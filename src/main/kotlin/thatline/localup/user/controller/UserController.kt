package thatline.localup.user.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import thatline.localup.common.response.BaseResponse
import thatline.localup.user.dto.FindBusinessDto
import thatline.localup.user.exception.BusinessAlreadyRegisteredException
import thatline.localup.user.exception.BusinessNotRegisteredException
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.request.RegisterBusinessRequest
import thatline.localup.user.request.UpdateBusinessRequest
import thatline.localup.user.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/business")
    fun findBusiness(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<BaseResponse<FindBusinessDto>> {
        val findBusinessDto = userService.findBusiness(userId)

        return ResponseEntity.ok(
            BaseResponse.success(
                data = findBusinessDto,
            )
        )
    }

    @PostMapping("/business")
    fun registerBusiness(
        @AuthenticationPrincipal userId: String,
        @RequestBody @Valid request: RegisterBusinessRequest,
    ): ResponseEntity<BaseResponse<Unit>> {
        userService.registerBusiness(
            userId = userId,
            businessName = request.businessName,
            businessSigunguCode = request.businessSigunguCode,
            businessZipCode = request.businessZipCode,
            businessAddress = request.businessAddress,
            businessAddressDetail = request.businessAddressDetail,
            businessLatitude = request.businessLatitude,
            businessLongitude = request.businessLongitude,
            businessType = request.businessType,
            businessItem = request.businessItem,
            businessAverageOrderAmount = request.businessAverageOrderAmount,
            businessSeatCount = request.businessSeatCount,
            businessCustomerSegments = request.businessCustomerSegments,
        )

        return ResponseEntity.ok(BaseResponse.success())
    }

    @PatchMapping("/business")
    fun updateBusiness(
        @AuthenticationPrincipal userId: String,
        @RequestBody @Valid request: UpdateBusinessRequest,
    ): ResponseEntity<BaseResponse<Unit>> {
        userService.updateBusiness(
            userId = userId,
            businessName = request.businessName,
            businessSigunguCode = request.businessSigunguCode,
            businessZipCode = request.businessZipCode,
            businessAddress = request.businessAddress,
            businessAddressDetail = request.businessAddressDetail,
            businessLatitude = request.businessLatitude,
            businessLongitude = request.businessLongitude,
            businessType = request.businessType,
            businessItem = request.businessItem,
            businessAverageOrderAmount = request.businessAverageOrderAmount,
            businessSeatCount = request.businessSeatCount,
            businessCustomerSegments = request.businessCustomerSegments,
        )

        return ResponseEntity.ok(BaseResponse.success())
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

    @ExceptionHandler(BusinessAlreadyRegisteredException::class)
    fun handleBusinessAlreadyRegisteredException(exception: BusinessAlreadyRegisteredException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                BaseResponse.failure(
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(BusinessNotRegisteredException::class)
    fun handleBusinessNotRegisteredException(exception: BusinessNotRegisteredException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                BaseResponse.failure(
                    message = exception.message
                )
            )
    }
}
