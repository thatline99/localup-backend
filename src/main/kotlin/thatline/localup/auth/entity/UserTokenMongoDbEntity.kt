package thatline.localup.auth.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user_token")
class UserTokenMongoDbEntity(
    val accessToken: String,

    val userId: String,

    val createdDate: LocalDateTime,

    val accessTokenExpirationDate: LocalDateTime,
)
