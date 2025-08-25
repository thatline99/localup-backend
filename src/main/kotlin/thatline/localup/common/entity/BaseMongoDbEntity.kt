package thatline.localup.common.entity

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

abstract class BaseMongoDbEntity(
    @Id
    val id: String,

    val createdDate: LocalDateTime,

    val lastModifiedDate: LocalDateTime,
)
