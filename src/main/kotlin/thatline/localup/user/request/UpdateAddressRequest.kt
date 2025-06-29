package thatline.localup.user.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import thatline.localup.common.annotation.NotBlankIfNotNull

data class UpdateAddressRequest(
    @field:NotBlank
    val zipCode: String,

    @field:NotBlank
    val address: String,

    @field:NotBlankIfNotNull
    val addressDetail: String?,

    @field:DecimalMin(value = "-90.0")
    @field:DecimalMax(value = "90.0")
    val latitude: Double,

    @field:DecimalMin(value = "-180.0")
    @field:DecimalMax(value = "180.0")
    val longitude: Double,
)
