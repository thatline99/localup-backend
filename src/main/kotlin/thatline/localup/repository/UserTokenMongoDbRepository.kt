package thatline.localup.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import thatline.localup.entity.UserTokenMongoDbEntity

@Repository
interface UserTokenMongoDbRepository : MongoRepository<UserTokenMongoDbEntity, String> {

    fun findByAccessToken(accessToken: String): UserTokenMongoDbEntity?

    fun deleteByAccessToken(accessToken: String)

    fun deleteByUserId(userId: String)
}
