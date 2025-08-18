package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.LastYearSameWeekVisitorStatisticsInformation
import thatline.localup.tourapi.dto.SigunguEventInformation

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val lastYearSameWeekVisitorStatisticsInformation: LastYearSameWeekVisitorStatisticsInformation,
    val sigunguEventInformation: SigunguEventInformation,
    val weatherInformation: WeatherInformation,
)
