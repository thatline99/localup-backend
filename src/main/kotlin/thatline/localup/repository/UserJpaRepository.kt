package thatline.localup.repository

import org.springframework.data.jpa.repository.JpaRepository
import thatline.localup.entity.UserJpaEntity

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun existsByEmail(email: String): Boolean
}
