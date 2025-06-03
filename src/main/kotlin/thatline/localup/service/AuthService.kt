package thatline.localup.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import thatline.localup.entity.UserJpaEntity
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.repository.UserJpaRepository

@Service
class AuthService(
    private val userJpaRepository: UserJpaRepository
) {
    @Transactional(readOnly = true)
    fun signIn(email: String, password: String) {
        // TODO: noah, custom exception
        val user = userJpaRepository.findByEmail(email)
            ?: throw IllegalArgumentException()

        // TODO: noah, hash password
        if (user.password != password) {
            throw IllegalArgumentException()
        }
    }

    @Transactional
    fun signUp(email: String, password: String) {
        if (userJpaRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        // TODO: noah, 비밀번호 암호화
        val newUser = UserJpaEntity(email = email, password = password)

        userJpaRepository.save(newUser)
    }
}
