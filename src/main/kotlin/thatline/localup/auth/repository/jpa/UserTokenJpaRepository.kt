package thatline.localup.auth.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import thatline.localup.auth.entity.jpa.UserTokenJpaEntity

interface UserTokenJpaRepository : JpaRepository<UserTokenJpaEntity, Long> {
    fun findByAccessToken(accessToken: String): UserTokenJpaEntity?

    fun deleteByAccessToken(accessToken: String)

    fun deleteByUserId(userId: Long)
}
