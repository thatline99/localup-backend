package thatline.localup.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class KakaoCheckRequest(
    @field:NotNull
    val kakaoId: String,

    @field:NotNull
    @field:Email
    val email: String,
)