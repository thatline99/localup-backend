package thatline.localup.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class KakaoSignupRequest(
    @field:NotNull
    val kakaoId: String,

    @field:NotNull
    @field:Email
    val email: String,

    @field:NotNull
    val name: String,

    val profileImage: String?,
)