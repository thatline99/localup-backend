package thatline.localup.repository.mongodb

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.entity.mongodb.UserMongoDbEntity

interface UserMongoDbRepository : MongoRepository<UserMongoDbEntity, String> {
    fun findByEmail(email: String): UserMongoDbEntity?

    fun existsByEmail(email: String): Boolean
}
