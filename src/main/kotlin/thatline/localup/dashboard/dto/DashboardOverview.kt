package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.LastYearSameWeekVisitorStatisticsInformation

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val lastYearSameWeekVisitorStatisticsInformation: LastYearSameWeekVisitorStatisticsInformation,
    val weatherInformation: WeatherInformation,
)
