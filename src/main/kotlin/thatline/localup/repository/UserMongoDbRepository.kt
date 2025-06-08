package thatline.localup.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import thatline.localup.entity.UserMongoDbEntity

@Repository
interface UserMongoDbRepository : MongoRepository<UserMongoDbEntity, String> {

    fun findByEmail(email: String): UserMongoDbEntity?

    fun existsByEmail(email: String): Boolean
}
