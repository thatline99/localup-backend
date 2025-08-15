package thatline.localup.user.repository

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.user.entity.UserMongoDbEntity

interface UserMongoDbRepository : MongoRepository<UserMongoDbEntity, String> {
    fun findByEmail(email: String): UserMongoDbEntity?

    fun existsByEmail(email: String): Boolean
}
