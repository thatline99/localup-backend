package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRanking

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingList: List<LastMonthlyTouristAttractionRanking>,
    val weatherInformation: WeatherInformation,
)
