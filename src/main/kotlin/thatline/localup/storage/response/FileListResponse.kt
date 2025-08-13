package thatline.localup.storage.response

data class FileListResponse(
    val files: List<FileInfo>,
    val totalCount: Int
)