package thatline.localup.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.entity.UserJpaEntity
import thatline.localup.entity.UserTokenJpaEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.repository.UserJpaRepository
import thatline.localup.repository.UserTokenJpaRepository
import java.util.*

@Service
class AuthService(
    private val userJpaRepository: UserJpaRepository,
    private val userTokenJpaRepository: UserTokenJpaRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun signIn(email: String, password: String): String {
        // TODO: noah, custom exception
        val user = userJpaRepository.findByEmail(email)
            ?: throw IllegalArgumentException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException()
        }

        userTokenJpaRepository.deleteByUserId(user.id)

        val accessToken = UUID.randomUUID().toString()

        // TODO: 토큰 맥스 없어
        val userToken = UserTokenJpaEntity(
            accessToken = accessToken,
            userId = user.id
        )

        userTokenJpaRepository.save(userToken)

        return accessToken
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
}
