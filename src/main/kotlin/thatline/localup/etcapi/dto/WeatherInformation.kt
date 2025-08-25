package thatline.localup.etcapi.dto

import java.time.LocalDateTime

data class WeatherInformation(
    val updatedDate: LocalDateTime,
    val dailyWeatherList: List<DailyWeather>,
)
