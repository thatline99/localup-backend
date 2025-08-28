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
    
    val phoneNumber: String? = null,
    
    val position: String? = null,

    val profileImage: String? = null,

    val isEmailVerified: Boolean = false,
    
    val isProfileCompleted: Boolean = false,
    
    val marketingConsent: Boolean = false,

    ) : BaseMongoDbEntity(id, createdDate, lastModifiedDate) {
    fun update(
        email: String = this.email,
        password: String? = this.password,
        role: Role = this.role,
        businessId: String? = this.businessId,
        kakaoId: String? = this.kakaoId,
        name: String? = this.name,
        phoneNumber: String? = this.phoneNumber,
        position: String? = this.position,
        profileImage: String? = this.profileImage,
        isEmailVerified: Boolean = this.isEmailVerified,
        isProfileCompleted: Boolean = this.isProfileCompleted,
        marketingConsent: Boolean = this.marketingConsent,
    ): UserMongoDbEntity {
        return UserMongoDbEntity(
            id = this.id,
            createdDate = this.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            email = email,
            password = password,
            role = role,
            businessId = businessId,
            kakaoId = kakaoId,
            name = name,
            phoneNumber = phoneNumber,
            position = position,
            profileImage = profileImage,
            isEmailVerified = isEmailVerified,
            isProfileCompleted = isProfileCompleted,
            marketingConsent = marketingConsent,
        )
    }
}
