package thatline.localup.auth.repository.mongodb

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.auth.entity.mongodb.UserTokenMongoDbEntity

interface UserTokenMongoDbRepository : MongoRepository<UserTokenMongoDbEntity, String> {
    fun findByAccessToken(accessToken: String): UserTokenMongoDbEntity?

    fun deleteByAccessToken(accessToken: String)

    fun deleteByUserId(userId: String)
}
