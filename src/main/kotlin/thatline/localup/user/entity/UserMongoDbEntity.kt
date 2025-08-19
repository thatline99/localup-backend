package thatline.localup.user.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import thatline.localup.common.constant.Role
import thatline.localup.common.entity.BaseMongoDbEntity
import java.time.LocalDateTime

@Document(collection = "user")
class UserMongoDbEntity(
    id: String = ObjectId().toHexString(),

    createdDate: LocalDateTime = LocalDateTime.now(),

    lastModifiedDate: LocalDateTime = createdDate,

    val email: String,

    val password: String? = null,

    val role: Role,

    @Indexed(unique = true, sparse = true)
    val businessId: String?,

    val kakaoId: String? = null,

    val name: String? = null,

    val profileImage: String? = null,

    val isActive: Boolean = true,

    val isEmailVerified: Boolean = false,

    ) : BaseMongoDbEntity(id, createdDate, lastModifiedDate)
