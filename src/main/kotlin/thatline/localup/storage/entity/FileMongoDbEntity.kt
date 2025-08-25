package thatline.localup.storage.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "file")
data class FileMongoDbEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    
    val userId: String,
    
    val fileName: String,
    
    val fileType: String,
    
    val fileSize: Long,
    
    val filePath: String,
    
    val deleted: Boolean = false,
    
    val createdDate: LocalDateTime = LocalDateTime.now(),
    
    val lastModifiedDate: LocalDateTime = LocalDateTime.now(),
    
    val deletedAt: LocalDateTime? = null
)