package thatline.localup.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.dto.AuthToken
import thatline.localup.entity.mongodb.UserMongoDbEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.exception.InvalidCredentialsException
import thatline.localup.repository.mongodb.UserMongoDbRepository
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

        val newUser = UserMongoDbEntity(email = email, password = hashedPassword)

        userRepository.save(newUser)
    }

    fun findUserIdByAccessToken(accessToken: String): String? {
        return userTokenRedisService.findUserIdByAccessToken(accessToken)
    }
}
