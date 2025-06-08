package thatline.localup.entity

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user")
class UserMongoDbEntity(
    @Id
    val id: String = ObjectId().toHexString(),

    val email: String,

    val password: String,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val lastModifiedDate: LocalDateTime = createdDate,
)
