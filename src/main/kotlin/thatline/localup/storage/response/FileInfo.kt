package thatline.localup.storage.response

import java.time.LocalDateTime

data class FileInfo(
    val fileId: String,
    val fileName: String,
    val fileType: String,
    val fileSize: Long,
    val uploadedAt: LocalDateTime
)