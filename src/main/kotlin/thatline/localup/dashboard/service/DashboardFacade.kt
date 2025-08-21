package thatline.localup.dashboard.service

import org.springframework.stereotype.Service
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.dashboard.dto.DashboardOverview
import thatline.localup.etcapi.service.WeatherService
import thatline.localup.tourapi.dto.SigunguEventWithDates
import thatline.localup.tourapi.service.TouristAttractionService
import thatline.localup.user.service.UserService

@Service
class DashboardFacade(
    private val userService: UserService,
    private val touristAttractionService: TouristAttractionService,
    private val weatherService: WeatherService,
) {
    // TODO-noah: rename sigunguCode -> legalDongSigunguCode
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

        val mainSigunguEvent = touristAttractionService.findMainSigunguEvent(
            legalDongSigunguCode = foundUserBusinessDto.sigunguCode,
            latitude = foundUserBusinessDto.latitude,
            longitude = foundUserBusinessDto.longitude,
        )

        val ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation =
            touristAttractionService.findOngoingOrUpComingSigunguEventsFromTodayToMonthEnd(
                legalDongSigunguCode = foundUserBusinessDto.sigunguCode,
            )

        val weatherInformation = weatherService.getThreeDayWeatherSummaries(
            sigunguCode = foundUserBusinessDto.sigunguCode
        )

        return DashboardOverview(
            lastMonthlyTouristAttractionRankingInformation = lastMonthlyTouristAttractionRankingInformation,
            lastYearSameWeekVisitorStatisticsInformation = lastYearSameWeekVisitorStatisticsInformation,
            mainSigunguEvent = mainSigunguEvent,
            ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation = ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation,
            weatherInformation = weatherInformation,
        )
    }

    // TODO: 다시 확인
    fun findMainEvent(
        legalDongSigunguCode: String,
    ): SigunguEventWithDates? {
        return touristAttractionService.findMainSigunguEvent(
            legalDongSigunguCode = legalDongSigunguCode,
            latitude = 0.0,
            longitude = 0.0,
        )
    }
}
