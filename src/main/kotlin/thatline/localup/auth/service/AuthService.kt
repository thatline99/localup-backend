package thatline.localup.auth.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.auth.dto.AuthToken
import thatline.localup.auth.dto.UserDetails
import thatline.localup.auth.exception.AccountDisabledException
import thatline.localup.auth.exception.DuplicateEmailException
import thatline.localup.auth.exception.EmailAlreadyExistsException
import thatline.localup.auth.exception.InvalidCredentialsException
import thatline.localup.auth.exception.UserNotFoundException
import thatline.localup.common.constant.Role
import thatline.localup.user.entity.UserMongoDbEntity
import thatline.localup.user.repository.UserMongoDbRepository
import java.util.*

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserMongoDbRepository,
    private val userTokenRedisService: UserTokenRedisService,
) {
    fun signIn(email: String, password: String): AuthToken {
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException()
        }

        val accessToken = UUID.randomUUID().toString()

        userTokenRedisService.save(accessToken, user.id)

        return AuthToken(accessToken)
    }

    fun signOut(accessToken: String) {
        userTokenRedisService.deleteByAccessToken(accessToken)
    }

    @Transactional
    fun signUp(email: String, password: String) {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val hashedPassword = passwordEncoder.encode(password)

        val newUser = UserMongoDbEntity(
            email = email,
            password = hashedPassword,
            role = Role.USER,
            businessId = null,
        )

        userRepository.save(newUser)
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
        val user = userRepository.findByKakaoIdAndEmail(kakaoId, email)
            ?: throw UserNotFoundException()

        if (!user.isActive) {
            throw AccountDisabledException()
        }

        val accessToken = UUID.randomUUID().toString()
        userTokenRedisService.save(accessToken, user.id)

        return AuthToken(accessToken)
    }

    @Transactional
    fun signUpKakaoUser(kakaoId: String, email: String, name: String, profileImage: String?): AuthToken {
        if (userRepository.existsByEmail(email)) {
            throw EmailAlreadyExistsException()
        }

        val newUser = UserMongoDbEntity(
            email = email,
            password = null,
            role = Role.USER,
            businessId = null,
            kakaoId = kakaoId,
            name = name,
            profileImage = profileImage,
            isActive = true,
        )

        val savedUser = userRepository.save(newUser)

        val accessToken = UUID.randomUUID().toString()
        userTokenRedisService.save(accessToken, savedUser.id)

        return AuthToken(accessToken)
    }
}
