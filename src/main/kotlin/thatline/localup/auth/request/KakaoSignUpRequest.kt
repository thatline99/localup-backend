package thatline.localup.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class KakaoSignUpRequest(
    @field:NotBlank
    val kakaoId: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val name: String,
    val profileImage: String?,
)
