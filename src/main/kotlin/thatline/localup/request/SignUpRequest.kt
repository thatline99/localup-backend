package thatline.localup.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
)
