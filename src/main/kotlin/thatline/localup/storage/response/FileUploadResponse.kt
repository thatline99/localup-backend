package thatline.localup.storage.response

import java.time.LocalDateTime

data class FileUploadResponse(
    val fileName: String,
    val uploadedAt: LocalDateTime
)