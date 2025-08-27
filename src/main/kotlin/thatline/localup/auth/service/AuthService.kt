package thatline.localup.auth.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import thatline.localup.auth.dto.AuthToken
import thatline.localup.auth.dto.UserDetails
import thatline.localup.auth.dto.LastLoginInfo
import thatline.localup.auth.exception.*
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.common.constant.Role
import thatline.localup.user.entity.UserMongoDbEntity
import thatline.localup.user.repository.UserMongoDbRepository
import thatline.localup.auth.entity.LoginHistoryMongoDbEntity
import thatline.localup.auth.entity.LoginMethod
import thatline.localup.auth.repository.LoginHistoryMongoDbRepository
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserMongoDbRepository,
    private val userTokenRedisService: UserTokenRedisService,
    private val emailService: EmailService,
    private val emailVerificationRedisService: EmailVerificationRedisService,
    private val loginHistoryRepository: LoginHistoryMongoDbRepository,
) {
    @CountMongoDbCommands
    fun signIn(email: String, password: String, ipAddress: String? = null, userAgent: String? = null): AuthToken {
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.password)) {
            // 실패한 로그인 기록 저장
            saveLoginHistory(email, LoginMethod.EMAIL, ipAddress, userAgent, false)
            throw InvalidCredentialsException()
        }

        // 이메일 인증 상태 확인 (카카오 사용자는 자동으로 인증 완료)
        if (!user.isEmailVerified) {
            throw EmailNotVerifiedException()
        }

        val accessToken = UUID.randomUUID().toString()

        userTokenRedisService.save(accessToken, user.id)
        
        // 성공한 로그인 기록 저장
        saveLoginHistory(email, LoginMethod.EMAIL, ipAddress, userAgent, true)

        return AuthToken(accessToken)
    }
    
    private fun saveLoginHistory(
        email: String,
        loginMethod: LoginMethod,
        ipAddress: String?,
        userAgent: String?,
        success: Boolean
    ) {
        try {
            val deviceType = detectDeviceType(userAgent)
            
            val history = LoginHistoryMongoDbEntity(
                email = email,
                loginMethod = loginMethod,
                ipAddress = ipAddress,
                userAgent = userAgent,
                deviceType = deviceType,
                loginSuccess = success
            )
            
            loginHistoryRepository.save(history)
            
            // 각 사용자당 최근 10개 기록만 유지
            cleanupOldLoginHistory(email)
        } catch (e: Exception) {
            // 로그인 기록 저장 실패는 로그인 프로세스를 중단시키지 않음
            // 로그만 남기고 계속 진행
        }
    }
    
    private fun detectDeviceType(userAgent: String?): String {
        if (userAgent == null) return "unknown"
        
        val lowerCaseUA = userAgent.lowercase()
        return when {
            lowerCaseUA.contains("mobile") || lowerCaseUA.contains("android") || 
            lowerCaseUA.contains("iphone") -> "mobile"
            lowerCaseUA.contains("tablet") || lowerCaseUA.contains("ipad") -> "tablet"
            else -> "desktop"
        }
    }
    
    private fun cleanupOldLoginHistory(email: String) {
        val count = loginHistoryRepository.countByEmail(email)
        if (count > 10) {
            // 30일 이전 기록 삭제
            val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
            loginHistoryRepository.deleteByEmailAndCreatedDateBefore(email, thirtyDaysAgo)
        }
    }

    fun signOut(accessToken: String) {
        userTokenRedisService.deleteByAccessToken(accessToken)
    }

    @CountMongoDbCommands
    fun signUp(email: String, password: String, marketingConsent: Boolean, baseUrl: String) {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val hashedPassword = passwordEncoder.encode(password)

        val newUser = UserMongoDbEntity(
            email = email,
            password = hashedPassword,
            role = Role.USER,
            businessId = null,
            isEmailVerified = false,
            marketingConsent = marketingConsent,
        )

        userRepository.save(newUser)

        // 이메일 인증 링크 발송
        emailService.sendVerificationEmail(email, baseUrl)
    }

    @CountMongoDbCommands
    fun getLastLoginInfo(email: String): LastLoginInfo? {
        val lastSuccessfulLogin = loginHistoryRepository.findFirstByEmailAndLoginSuccessOrderByCreatedDateDesc(email, true)
        
        if (lastSuccessfulLogin == null) {
            // 로그인 기록이 없으면 첫 방문자
            return LastLoginInfo(
                email = email,
                lastLoginMethod = "NONE",
                lastLoginTime = LocalDateTime.now(),
                deviceType = null,
                isFirstTime = true
            )
        }
        
        return LastLoginInfo(
            email = email,
            lastLoginMethod = lastSuccessfulLogin.loginMethod.name,
            lastLoginTime = lastSuccessfulLogin.createdDate,
            deviceType = lastSuccessfulLogin.deviceType,
            isFirstTime = false
        )
    }
    
    fun findUserDetailsByAccessToken(accessToken: String): UserDetails? {
        val userId = userTokenRedisService.findUserIdByAccessToken(accessToken)
            ?: return null

        val user = userRepository.findByIdOrNull(userId)
            ?: return null

        return UserDetails(
            id = user.id,
            role = user.role
        )
    }

    fun checkKakaoUser(kakaoId: String, email: String): AuthToken {
        val user = userRepository.findByKakaoId(kakaoId)
            ?: throw UserNotFoundException()

        val accessToken = UUID.randomUUID().toString()
        userTokenRedisService.save(accessToken, user.id)

        return AuthToken(accessToken)
    }

    fun signUpKakaoUser(kakaoId: String, email: String, name: String, profileImage: String?): AuthToken {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val newUser =
            UserMongoDbEntity(
                email = email,
                password = null,
                role = Role.USER,
                businessId = null,
                kakaoId = kakaoId,
                name = name,
                profileImage = profileImage,
                isEmailVerified = true,
            )

        val savedUser = userRepository.save(newUser)

        val accessToken = UUID.randomUUID().toString()
        userTokenRedisService.save(accessToken, savedUser.id)

        return AuthToken(accessToken)
    }

    fun verifyEmail(email: String, token: String) {
        // 토큰 유효성 검증
        if (!emailVerificationRedisService.isValidVerificationToken(email, token)) {
            throw InvalidVerificationTokenException()
        }
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException()

        val updatedUser = UserMongoDbEntity(
            id = user.id,
            createdDate = user.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            email = user.email,
            password = user.password,
            role = user.role,
            businessId = user.businessId,
            kakaoId = user.kakaoId,
            name = user.name,
            profileImage = user.profileImage,
            isEmailVerified = true,
        )

        userRepository.save(updatedUser)
        
        // 인증 완료 후 Redis에서 토큰 삭제
        emailVerificationRedisService.deleteVerificationToken(email)
    }
}
