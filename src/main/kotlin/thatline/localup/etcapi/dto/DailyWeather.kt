package thatline.localup.etcapi.dto

import java.time.LocalDate

data class DailyWeather(
    val date: LocalDate,
    val condition: WeatherCondition,
    val minimumTemperature: Double,
    val maximumTemperature: Double,
)
