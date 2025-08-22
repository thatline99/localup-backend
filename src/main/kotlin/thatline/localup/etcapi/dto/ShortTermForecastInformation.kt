package thatline.localup.etcapi.dto

import java.time.LocalDateTime

data class ShortTermForecastInformation(
    val updatedDate: LocalDateTime,
    val shortTermForecasts: List<ShortTermForecast>,
)
