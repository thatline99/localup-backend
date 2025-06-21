package thatline.localup.user.entity.mongodb

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import thatline.localup.common.entity.mongodb.BaseMongoDbEntity
import java.time.LocalDateTime

@Document(collection = "user")
class UserMongoDbEntity(
    id: String = ObjectId().toHexString(),

    createdDate: LocalDateTime = LocalDateTime.now(),

    lastModifiedDate: LocalDateTime = createdDate,

    val email: String,

    val password: String,
) : BaseMongoDbEntity(id, createdDate, lastModifiedDate)
