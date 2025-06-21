package thatline.localup.localup.response.dto

import java.time.LocalDate

data class TouristAttractionConcentrationRateLast30Days(
    val areaCode: String,
    val areaName: String,
    val signguCode: String,
    val signguName: String,
    val concentrationRates: List<ConcentrationRate>,
) {
    data class ConcentrationRate(
        val date: LocalDate,
        val rate: Double,
    )
}
