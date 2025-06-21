package thatline.localup.user.entity.jpa

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "`user`")
class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, length = 60)
    val password: String,

    @CreatedDate
    @Column(updatable = false)
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val lastModifiedDate: LocalDateTime = createdDate,
)
