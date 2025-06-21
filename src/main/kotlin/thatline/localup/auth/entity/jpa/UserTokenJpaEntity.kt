package thatline.localup.auth.entity.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_token")
class UserTokenJpaEntity(
    @Id
    val accessToken: String,

    @Column(nullable = false, unique = true)
    val userId: Long,

    @Column(nullable = false, updatable = false)
    val createdDate: LocalDateTime,

    @Column(nullable = false, updatable = false)
    val accessTokenExpirationDate: LocalDateTime,
)
