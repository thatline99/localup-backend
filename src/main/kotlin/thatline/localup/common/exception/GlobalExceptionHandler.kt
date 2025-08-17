package thatline.localup.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import thatline.localup.auth.exception.AccountDisabledException
import thatline.localup.auth.exception.EmailAlreadyExistsException
import thatline.localup.auth.exception.UserNotFoundException
import thatline.localup.common.response.BaseResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<BaseResponse<Unit>> {
        val errorMessage = exception.bindingResult
            .fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage ?: "Invalid"}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.failure(message = errorMessage))
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException,
    ): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(BaseResponse.failure(message = exception.message ?: "User not found"))
    }

    @ExceptionHandler(AccountDisabledException::class)
    fun handleAccountDisabledException(
        exception: AccountDisabledException,
    ): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(BaseResponse.failure(message = exception.message ?: "Account is disabled"))
    }

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(
        exception: EmailAlreadyExistsException,
    ): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(BaseResponse.failure(message = exception.message ?: "Email already exists"))
    }
}
