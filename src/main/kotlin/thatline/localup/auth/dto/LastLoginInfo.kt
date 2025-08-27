package thatline.localup.auth.dto

import java.time.LocalDateTime

data class LastLoginInfo(
    val email: String,
    val lastLoginMethod: String,
    val lastLoginTime: LocalDateTime,
    val deviceType: String?,
    val isFirstTime: Boolean
)