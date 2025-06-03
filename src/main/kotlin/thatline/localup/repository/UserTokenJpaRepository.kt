package thatline.localup.repository

import org.springframework.data.jpa.repository.JpaRepository
import thatline.localup.entity.UserTokenJpaEntity

interface UserTokenJpaRepository : JpaRepository<UserTokenJpaEntity, Long> {
    fun findByAccessToken(accessToken: String): UserTokenJpaEntity?

    fun deleteByUserId(userId: Long)
}
