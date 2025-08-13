package thatline.localup.storage.controller

import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import thatline.localup.common.annotation.RequireUser
import thatline.localup.common.response.BaseResponse
import thatline.localup.storage.response.FileListResponse
import thatline.localup.storage.response.FileUploadResponse
import thatline.localup.storage.service.StorageService
import java.net.URLEncoder

@RestController
@RequestMapping("/api/storage")
class StorageController(
    private val storageService: StorageService,
) {
    @PostMapping("/upload")
    fun uploadFile(
        @AuthenticationPrincipal userId: String,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<BaseResponse<FileUploadResponse>> {
        val uploadedFile = storageService.uploadFile(userId, file)
        
        return ResponseEntity.ok(BaseResponse.success(uploadedFile))
    }
    
    @GetMapping("/files")
    fun getUserFiles(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<BaseResponse<FileListResponse>> {
        val fileList = storageService.getUserFiles(userId)
        
        return ResponseEntity.ok(BaseResponse.success(fileList))
    }
    
    @DeleteMapping("/files/{fileId}")
    fun deleteFile(
        @AuthenticationPrincipal userId: String,
        @PathVariable fileId: String,
    ): ResponseEntity<BaseResponse<Unit>> {
        val isDeleted = storageService.deleteFile(userId, fileId)
        
        return if (isDeleted) {
            ResponseEntity.ok(BaseResponse.success())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/files/{fileId}/download")
    fun downloadFile(
        @AuthenticationPrincipal userId: String,
        @PathVariable fileId: String,
    ): ResponseEntity<Resource> {
        val fileResource = storageService.downloadFile(userId, fileId)
            ?: return ResponseEntity.notFound().build()
        
        val fileName = fileResource.second
        val encodedFileName = URLEncoder.encode(fileName, "UTF-8")
            .replace("+", "%20")
        
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${encodedFileName}\"; filename*=UTF-8''${encodedFileName}"
            )
            .body(fileResource.first)
    }
}