package thatline.localup.auth.repository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import thatline.localup.auth.entity.LoginHistoryMongoDbEntity

@Repository
interface LoginHistoryMongoDbRepository : MongoRepository<LoginHistoryMongoDbEntity, String> {
    
    fun findByEmailOrderByCreatedDateDesc(email: String, pageable: PageRequest): List<LoginHistoryMongoDbEntity>
    
    fun findFirstByEmailAndLoginSuccessOrderByCreatedDateDesc(email: String, loginSuccess: Boolean): LoginHistoryMongoDbEntity?
    
    fun countByEmail(email: String): Long
    
    fun deleteByEmailAndCreatedDateBefore(email: String, date: java.time.LocalDateTime)
}