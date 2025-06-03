package thatline.localup.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.dto.AuthToken
import thatline.localup.entity.UserJpaEntity
import thatline.localup.entity.UserTokenJpaEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.exception.InvalidCredentialsException
import thatline.localup.property.TokenProperty
import thatline.localup.repository.UserJpaRepository
import thatline.localup.repository.UserTokenJpaRepository
import java.time.LocalDateTime
import java.util.*

// TODO: signService 분리
@Service
class AuthService(
    private val userJpaRepository: UserJpaRepository,
    private val userTokenJpaRepository: UserTokenJpaRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProperty: TokenProperty,
) {
    @Transactional
    fun signIn(email: String, password: String): AuthToken {
        val user = userJpaRepository.findByEmail(email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException()
        }

        userTokenJpaRepository.deleteByUserId(user.id)

        val accessToken = UUID.randomUUID().toString()
        val createdDate = LocalDateTime.now()
        val accessTokenExpirationDate = createdDate.plusSeconds(tokenProperty.accessToken.expirationSeconds.toLong())

        val userToken = UserTokenJpaEntity(accessToken, user.id, createdDate, accessTokenExpirationDate)

        userTokenJpaRepository.save(userToken)

        return AuthToken(accessToken)
    }

    @Transactional
    fun signUp(email: String, password: String) {
        if (userJpaRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val hashedPassword = passwordEncoder.encode(password)

        val newUser = UserJpaEntity(email = email, password = hashedPassword)

        userJpaRepository.save(newUser)
    }

    @Transactional
    fun findUserIdByAccessToken(accessToken: String): Long? {
        val userToken = userTokenJpaRepository.findByAccessToken(accessToken) ?: return null

        if (userToken.accessTokenExpirationDate.isBefore(LocalDateTime.now())) {
            userTokenJpaRepository.deleteByAccessToken(accessToken)

            return null
        }

        return userToken.userId
    }
}
