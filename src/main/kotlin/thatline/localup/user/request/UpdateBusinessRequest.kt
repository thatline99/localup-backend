package thatline.localup.user.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import thatline.localup.common.annotation.NotBlankIfNotNull

data class UpdateBusinessRequest(
    @field:NotBlank
    val businessName: String,
    @field:NotBlank
    val businessZipCode: String,
    @field:NotBlank
    val businessAddress: String,
    @field:NotBlankIfNotNull
    val businessAddressDetail: String?,
    @field:DecimalMin(value = "-90.0")
    @field:DecimalMax(value = "90.0")
    val businessLatitude: Double,
    @field:DecimalMin(value = "-180.0")
    @field:DecimalMax(value = "180.0")
    val businessLongitude: Double,
    @field:NotBlank
    val businessType: String,
    @field:NotBlank
    val businessItem: String,
    @field:DecimalMin(value = "0.0", inclusive = true)
    val businessAverageOrderAmount: Double,
    @field:Min(value = 0)
    val businessSeatCount: Int,
)
