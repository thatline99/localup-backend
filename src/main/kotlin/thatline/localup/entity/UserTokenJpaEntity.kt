package thatline.localup.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "user_token")
class UserTokenJpaEntity(
    @Id
    @Column(nullable = false, unique = true)
    val accessToken: String,

    @Column(nullable = false, unique = true)
    val userId: Long,

    @CreatedDate
    @Column(updatable = false)
    val createdDate: LocalDateTime = LocalDateTime.now()
)
