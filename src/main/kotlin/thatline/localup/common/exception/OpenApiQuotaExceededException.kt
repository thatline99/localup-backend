package thatline.localup.common.exception

class OpenApiQuotaExceededException(
    val name: String,
    val limit: Long,
    val currentCount: Long,
    cause: Throwable? = null,
) : BaseException("OPEN_API_QUOTA_EXCEEDED, name: $name, limit: $limit, currentCount: $currentCount", cause)
