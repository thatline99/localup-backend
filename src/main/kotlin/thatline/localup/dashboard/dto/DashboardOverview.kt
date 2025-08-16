package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val weatherInformation: WeatherInformation,
)
