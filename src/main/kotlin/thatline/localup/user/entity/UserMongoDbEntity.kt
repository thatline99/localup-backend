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

    val password: String,

    val role: Role,

    @Indexed(unique = true, sparse = true)
    val businessId: String?,
) : BaseMongoDbEntity(id, createdDate, lastModifiedDate) {
    fun update(
        email: String = this.email,
        password: String = this.password,
        role: Role = this.role,
        businessId: String? = this.businessId,
    ): UserMongoDbEntity {
        return UserMongoDbEntity(
            id = this.id,
            createdDate = this.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            email = email,
            password = password,
            role = role,
            businessId = businessId,
        )
    }
}
