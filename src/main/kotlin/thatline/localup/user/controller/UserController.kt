package thatline.localup.user.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.request.UpdateAddressRequest
import thatline.localup.user.service.UserService

// TODO: BaseResponse에 data가 없는 경우, 고려 필요

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PutMapping("/address")
    fun updateAddress(
        @AuthenticationPrincipal id: String,
        @RequestBody request: UpdateAddressRequest,
    ): ResponseEntity<Void> {
        userService.updateAddress(
            id = id,
            zipCode = request.zipCode,
            address = request.address,
            addressDetail = request.addressDetail,
            latitude = request.latitude,
            longitude = request.longitude
        )

        return ResponseEntity.ok().build()
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<Void> {
        return ResponseEntity.notFound().build()
    }
}
