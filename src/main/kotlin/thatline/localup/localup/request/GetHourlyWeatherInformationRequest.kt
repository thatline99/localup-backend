package thatline.localup.localup.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin

data class GetHourlyWeatherInformationRequest(
    @field:DecimalMin(value = "-90.0")
    @field:DecimalMax(value = "90.0")
    val latitude: Double,

    @field:DecimalMin(value = "-180.0")
    @field:DecimalMax(value = "180.0")
    val longitude: Double,
)
