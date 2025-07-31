package thatline.localup.auth.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.auth.dto.AuthToken
import thatline.localup.auth.dto.UserDetails
import thatline.localup.auth.exception.DuplicateEmailException
import thatline.localup.auth.exception.InvalidCredentialsException
import thatline.localup.common.constant.Role
import thatline.localup.user.entity.mongodb.UserMongoDbEntity
import thatline.localup.user.repository.mongodb.UserMongoDbRepository
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
            zipCode = null,
            address = null,
            addressDetail = null,
            latitude = null,
            longitude = null,
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
}
