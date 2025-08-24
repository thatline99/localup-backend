package thatline.localup.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class KakaoCheckRequest(
    @field:NotBlank
    val kakaoId: String,
    @field:NotBlank
    @field:Email
    val email: String,
)
