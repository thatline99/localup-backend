package thatline.localup.auth.repository

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.auth.entity.UserTokenMongoDbEntity

interface UserTokenMongoDbRepository : MongoRepository<UserTokenMongoDbEntity, String> {
    fun findByAccessToken(accessToken: String): UserTokenMongoDbEntity?

    fun deleteByAccessToken(accessToken: String)

    fun deleteByUserId(userId: String)
}
