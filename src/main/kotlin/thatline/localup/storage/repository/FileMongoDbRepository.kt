package thatline.localup.storage.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import thatline.localup.storage.entity.FileMongoDbEntity

@Repository
interface FileMongoDbRepository : MongoRepository<FileMongoDbEntity, String> {
    fun findByUserIdAndDeletedFalseOrderByCreatedDateDesc(userId: String): List<FileMongoDbEntity>
    
    fun findByIdAndUserIdAndDeletedFalse(id: String, userId: String): FileMongoDbEntity?
}