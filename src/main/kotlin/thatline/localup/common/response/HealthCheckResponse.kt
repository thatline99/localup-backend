package thatline.localup.common.response

import java.time.LocalDateTime

data class HealthCheckResponse(
    val status: String,
    val timestamp: LocalDateTime,
    val service: String
)