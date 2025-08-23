package thatline.localup.etcapi.dto

import java.time.LocalDate

data class ShortTermForecast(
    val date: LocalDate,

    val dailyMinimumTemperature: Double?, // TMN (일 최저기온, ℃)
    val dailyMaximumTemperature: Double?, // TMX (일 최고기온, ℃)

    val hourlyShortTermForecasts: List<HourlyShortTermForecast>,
)
