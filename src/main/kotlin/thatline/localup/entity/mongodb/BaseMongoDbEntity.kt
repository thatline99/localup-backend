package thatline.localup.entity.mongodb

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
class BaseMongoDbEntity(
    @Id
    val id: String,

    val createdDate: LocalDateTime,

    val lastModifiedDate: LocalDateTime,
)
