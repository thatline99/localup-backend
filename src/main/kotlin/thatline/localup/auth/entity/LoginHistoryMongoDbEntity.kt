package thatline.localup.auth.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import thatline.localup.common.entity.BaseMongoDbEntity
import java.time.LocalDateTime

@Document(collection = "login_history")
class LoginHistoryMongoDbEntity(
    id: String = ObjectId().toHexString(),
    createdDate: LocalDateTime = LocalDateTime.now(),
    lastModifiedDate: LocalDateTime = createdDate,
    
    val email: String,
    val loginMethod: LoginMethod,
    val ipAddress: String?,
    val userAgent: String?,
    val deviceType: String?, // "mobile", "desktop", "tablet"
    val loginSuccess: Boolean,
) : BaseMongoDbEntity(id, createdDate, lastModifiedDate)

enum class LoginMethod {
    EMAIL,
    KAKAO
}