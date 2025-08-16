package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.VisitorStatistic

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val lastYearSameWeekVisitorStatistics: List<VisitorStatistic>,
    val weatherInformation: WeatherInformation,
)
