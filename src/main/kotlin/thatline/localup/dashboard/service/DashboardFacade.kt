package thatline.localup.dashboard.service

import org.springframework.stereotype.Service
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.dashboard.dto.DashboardOverview
import thatline.localup.etcapi.service.WeatherService
import thatline.localup.tourapi.service.TouristAttractionService
import thatline.localup.user.service.UserService

@Service
class DashboardFacade(
    private val userService: UserService,
    private val touristAttractionService: TouristAttractionService,
    private val weatherService: WeatherService,
) {
    @CountMongoDbCommands
    fun getDashboardOverview(userId: String): DashboardOverview {
        val foundUserBusinessDto = userService.findBusiness(userId)

        val lastMonthlyTouristAttractionRankingInformation =
            touristAttractionService.findLastMonthlyTouristAttractionRanking(
                areaCode = foundUserBusinessDto.sigunguCode.substring(0, 2),
                sigunguCode = foundUserBusinessDto.sigunguCode
            )

        val lastYearSameWeekVisitorStatisticsInformation =
            touristAttractionService.findLastYearSameWeekVisitorStatistics(
                sigunguCode = foundUserBusinessDto.sigunguCode
            )

        val sigunguMainEventInformation = touristAttractionService.findSigunguMainEvent(
            sigunguCode = foundUserBusinessDto.sigunguCode,
            latitude = foundUserBusinessDto.latitude,
            longitude = foundUserBusinessDto.longitude,
        )

        val ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation =
            touristAttractionService.findOngoingOrUpComingSigunguEventsFromTodayToMonthEnd(
                sigunguCode = foundUserBusinessDto.sigunguCode,
            )

        val weatherInformation = weatherService.findShortTermForecast(
            sigunguCode = foundUserBusinessDto.sigunguCode
        )

        return DashboardOverview(
            lastMonthlyTouristAttractionRankingInformation = lastMonthlyTouristAttractionRankingInformation,
            lastYearSameWeekVisitorStatisticsInformation = lastYearSameWeekVisitorStatisticsInformation,
            sigunguMainEventInformation = sigunguMainEventInformation,
            ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation = ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation,
            weatherInformation = weatherInformation,
        )
    }
}
