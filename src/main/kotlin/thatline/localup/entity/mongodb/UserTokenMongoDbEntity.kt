package thatline.localup.entity.mongodb

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user_token")
class UserTokenMongoDbEntity(
    @Id
    val accessToken: String,

    val userId: String,

    val createdDate: LocalDateTime,

    val accessTokenExpirationDate: LocalDateTime,
)
