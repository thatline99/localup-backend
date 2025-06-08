package thatline.localup.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import thatline.localup.entity.jpa.UserJpaEntity

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?

    fun existsByEmail(email: String): Boolean
}
