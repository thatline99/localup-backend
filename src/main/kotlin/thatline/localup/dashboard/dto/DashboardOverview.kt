package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.LastYearSameWeekVisitorStatisticsInformation
import thatline.localup.tourapi.dto.SigunguEventWithDates

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val lastYearSameWeekVisitorStatisticsInformation: LastYearSameWeekVisitorStatisticsInformation,
    val ongoingOrUpComingSigunguEventsFromTodayToMonthEnd: List<SigunguEventWithDates>,
    val weatherInformation: WeatherInformation,
)
