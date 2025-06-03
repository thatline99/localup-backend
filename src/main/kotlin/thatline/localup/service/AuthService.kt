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
    @Transactional
    fun signUp(email: String, password: String) {
        if (userJpaRepository.existsByEmail(email)) {
            throw DuplicateEmailException()
        }

        val newUser = UserJpaEntity(email = email, password = password)

        userJpaRepository.save(newUser)
    }
}
