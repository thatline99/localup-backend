package thatline.localup.storage.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import thatline.localup.storage.entity.FileMongoDbEntity
import thatline.localup.storage.property.StorageProperty
import thatline.localup.storage.repository.FileMongoDbRepository
import thatline.localup.storage.response.FileInfo
import thatline.localup.storage.response.FileListResponse
import thatline.localup.storage.response.FileUploadResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime

@Service
class StorageService(
    private val storageProperty: StorageProperty,
    private val fileRepository: FileMongoDbRepository,
) {
    fun uploadFile(userId: String, file: MultipartFile): FileUploadResponse {
        val userDirectory = createUserDirectory(userId)
        val originalFileName = file.originalFilename ?: "file"
        val uniqueFileName = generateUniqueFileName(userDirectory, originalFileName)
        val targetPath = userDirectory.resolve(uniqueFileName)
        
        // 파일 저장
        Files.copy(file.inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
        
        // DB에 파일 정보 저장
        val fileEntity = FileMongoDbEntity(
            userId = userId,
            fileName = uniqueFileName,
            fileType = getFileExtension(uniqueFileName),
            fileSize = file.size,
            filePath = targetPath.toString()
        )
        
        fileRepository.save(fileEntity)
        
        return FileUploadResponse(
            fileName = uniqueFileName,
            uploadedAt = fileEntity.createdDate
        )
    }
    
    private fun createUserDirectory(userId: String): Path {
        val userPath = Paths.get(storageProperty.basePath, "users", userId)
        
        if (!Files.exists(userPath)) {
            Files.createDirectories(userPath)
        }
        
        return userPath
    }
    
    private fun generateUniqueFileName(directory: Path, originalFileName: String): String {
        var fileName = originalFileName
        var counter = 1
        
        // 파일명과 확장자 분리
        val baseName = if (originalFileName.contains(".")) {
            originalFileName.substringBeforeLast(".")
        } else {
            originalFileName
        }
        
        val extension = if (originalFileName.contains(".")) {
            ".${originalFileName.substringAfterLast(".")}"
        } else {
            ""
        }
        
        // 중복 검사 및 번호 부여
        while (Files.exists(directory.resolve(fileName))) {
            fileName = "$baseName($counter)$extension"
            counter++
        }
        
        return fileName
    }
    
    fun getUserFiles(userId: String): FileListResponse {
        // DB에서 논리적으로 삭제되지 않은 파일만 조회
        val fileEntities = fileRepository.findByUserIdAndDeletedFalseOrderByCreatedDateDesc(userId)
        
        val files = fileEntities.map { entity ->
            FileInfo(
                fileId = entity.id,
                fileName = entity.fileName,
                fileType = entity.fileType,
                fileSize = entity.fileSize,
                uploadedAt = entity.createdDate
            )
        }
        
        return FileListResponse(
            files = files,
            totalCount = files.size
        )
    }
    
    fun deleteFile(userId: String, fileId: String): Boolean {
        // fileId와 userId로 파일 찾기
        val fileEntity = fileRepository.findByIdAndUserIdAndDeletedFalse(fileId, userId)
            ?: return false
        
        // 논리적 삭제 처리
        val deletedEntity = fileEntity.copy(
            deleted = true,
            deletedAt = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        
        fileRepository.save(deletedEntity)
        
        // 물리적 파일은 삭제하지 않음
        return true
    }
    
    private fun getFileExtension(fileName: String): String {
        return if (fileName.contains(".")) {
            fileName.substringAfterLast(".").uppercase()
        } else {
            "UNKNOWN"
        }
    }
    
    fun downloadFile(userId: String, fileId: String): Pair<Resource, String>? {
        // fileId와 userId로 파일 찾기
        val fileEntity = fileRepository.findByIdAndUserIdAndDeletedFalse(fileId, userId)
            ?: return null
        
        try {
            val path = Paths.get(fileEntity.filePath)
            val resource = UrlResource(path.toUri())
            
            if (resource.exists() && resource.isReadable) {
                return Pair(resource, fileEntity.fileName)
            }
        } catch (ex: Exception) {
            // 파일 읽기 실패 시 null 반환
        }
        
        return null
    }
}