package thatline.localup.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import thatline.localup.auth.validation.ValidPassword

data class SignUpRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:ValidPassword
    val password: String,
    
    val marketingConsent: Boolean = false,
)
