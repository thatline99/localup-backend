package thatline.localup.user.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import thatline.localup.common.response.BaseResponse
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.request.UpdateAddressRequest
import thatline.localup.user.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PutMapping("/address")
    fun updateAddress(
        @AuthenticationPrincipal id: String,
        @RequestBody @Valid request: UpdateAddressRequest,
    ): ResponseEntity<BaseResponse<Unit>> {
        userService.updateAddress(
            id = id,
            zipCode = request.zipCode,
            address = request.address,
            addressDetail = request.addressDetail,
            latitude = request.latitude,
            longitude = request.longitude
        )

        return ResponseEntity.ok(BaseResponse.success())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Unit>> {
        val errorMessage = exception.bindingResult
            .fieldErrors
            .joinToString("; ") { it.field + ": " + (it.defaultMessage ?: "Invalid") }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                BaseResponse.failure(
                    message = errorMessage
                )
            )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                BaseResponse.failure(
                    message = exception.message
                )
            )
    }
}
