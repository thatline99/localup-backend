package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.ShortTermForecastInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.LastYearSameWeekVisitorStatisticsInformation
import thatline.localup.tourapi.dto.OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation
import thatline.localup.tourapi.dto.SigunguMainEventInformation

data class DashboardOverview(
    val lastMonthlyTouristAttractionRankingInformation: LastMonthlyTouristAttractionRankingInformation,
    val lastYearSameWeekVisitorStatisticsInformation: LastYearSameWeekVisitorStatisticsInformation,
    val sigunguMainEventInformation: SigunguMainEventInformation,
    val ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation: OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation,
    // TODO: 분리
    val weatherInformation: ShortTermForecastInformation,
)
