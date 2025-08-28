package thatline.localup.user.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UpdateProfileRequest(
    @field:NotBlank(message = "이름을 입력해주세요")
    val name: String,
    
    @field:NotBlank(message = "전화번호를 입력해주세요")
    @field:Pattern(
        regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
        message = "올바른 전화번호 형식이 아닙니다 (예: 010-1234-5678)"
    )
    val phoneNumber: String,
    
    @field:NotBlank(message = "직책을 입력해주세요")
    val position: String,
)