package thatline.localup.entity

import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user_token")
class UserTokenMongoDbEntity(

    @Id
    val accessToken: String,

    val userId: String,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    val accessTokenExpirationDate: LocalDateTime,
)
