package thatline.localup.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.dto.AuthToken
import thatline.localup.entity.UserMongoDbEntity
import thatline.localup.entity.UserTokenMongoDbEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.exception.InvalidCredentialsException
import thatline.localup.property.TokenProperty
import thatline.localup.repository.UserMongoDbRepository
import thatline.localup.repository.UserTokenMongoDbRepository
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val userRepository: UserMongoDbRepository,
    private val userTokenRepository: UserTokenMongoDbRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProperty: TokenProperty,
) {
    @Transactional
    fun signIn(email: String, password: String): AuthToken {
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException()
        }

        userTokenRepository.deleteByUserId(user.id)

        val accessToken = UUID.randomUUID().toString()
        val createdDate = LocalDateTime.now()
        val accessTokenExpirationDate = createdDate.plusSeconds(tokenProperty.accessToken.expirationSeconds)

        val userToken = UserTokenMongoDbEntity(accessToken, user.id, createdDate, accessTokenExpirationDate)

        userTokenRepository.save(userToken)

        return AuthToken(accessToken)
    }

    @Transactional
    fun signOut(userId: String) {
        userTokenRepository.deleteByUserId(userId)
    }

    @Transactional
    fun signUp(email: String, password: String) {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val hashedPassword = passwordEncoder.encode(password)

        val newUser = UserMongoDbEntity(email = email, password = hashedPassword)

        userRepository.save(newUser)
    }

    @Transactional
    fun findUserIdByAccessToken(accessToken: String): String? {
        val userToken = userTokenRepository.findByAccessToken(accessToken) ?: return null

        if (userToken.accessTokenExpirationDate.isBefore(LocalDateTime.now())) {
            userTokenRepository.deleteByAccessToken(accessToken)

            return null
        }

        return userToken.userId
    }
}
