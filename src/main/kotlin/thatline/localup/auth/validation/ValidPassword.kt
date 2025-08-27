package thatline.localup.auth.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidator::class])
annotation class ValidPassword(
    val message: String = "비밀번호는 8자 이상이며, 영문과 숫자를 모두 포함해야 합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PasswordValidator : ConstraintValidator<ValidPassword, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        
        // 8자 이상
        if (value.length < 8) return false
        
        // 영문 포함 확인 (대소문자 구분 없음)
        val hasLetter = value.any { it.isLetter() }
        
        // 숫자 포함 확인
        val hasDigit = value.any { it.isDigit() }
        
        return hasLetter && hasDigit
    }
}