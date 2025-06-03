package thatline.localup.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.entity.UserJpaEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.repository.UserJpaRepository

@Service
class AuthService(
    private val userJpaRepository: UserJpaRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional(readOnly = true)
    fun signIn(email: String, password: String) {
        // TODO: noah, custom exception
        val user = userJpaRepository.findByEmail(email)
            ?: throw IllegalArgumentException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException()
        }
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
