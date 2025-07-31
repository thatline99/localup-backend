package thatline.localup.auth.dto

import thatline.localup.common.constant.Role

data class UserDetails(
    val id: String,
    val role: Role,
)
