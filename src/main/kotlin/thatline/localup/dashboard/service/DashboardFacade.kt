package thatline.localup.dashboard.service

import org.springframework.stereotype.Service
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.dashboard.dto.DashboardOverview
import thatline.localup.etcapi.dto.ShortTermForecastInformation
import thatline.localup.etcapi.service.ForecastService
import thatline.localup.tourapi.dto.VisitorStatisticsInformation
import thatline.localup.tourapi.service.TouristAttractionService
import thatline.localup.user.service.UserService
import java.time.LocalDate

@Service
class DashboardFacade(
    private val userService: UserService,
    private val touristAttractionService: TouristAttractionService,
    private val forecastService: ForecastService,
) {
    @CountMongoDbCommands
    fun getDashboardOverview(userId: String): DashboardOverview {
        val foundUserBusinessDto = userService.findBusiness(userId)

        val lastMonthlyTouristAttractionRankingInformation =
            touristAttractionService.findLastMonthlyTouristAttractionRanking(
                areaCode = foundUserBusinessDto.sigunguCode.substring(0, 2),
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

        return DashboardOverview(
            lastMonthlyTouristAttractionRankingInformation = lastMonthlyTouristAttractionRankingInformation,
            sigunguMainEventInformation = sigunguMainEventInformation,
            ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation = ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformation,
        )
    }

    @CountMongoDbCommands
    fun findShortTermForecast(userId: String): ShortTermForecastInformation {
        val foundUserBusinessDto = userService.findBusiness(userId)

        val shortTermForecastInformation = forecastService.findShortTermForecast(
            sigunguCode = foundUserBusinessDto.sigunguCode
        )

        return shortTermForecastInformation
    }

    @CountMongoDbCommands
    fun findVisitorStatistics(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): VisitorStatisticsInformation {
        val foundUserBusinessDto = userService.findBusiness(userId)

        val visitorStatisticInformation = touristAttractionService.findVisitorStatistics(
            sigunguCode = foundUserBusinessDto.sigunguCode,
            startDate = startDate,
            endDate = endDate,
        )

        return visitorStatisticInformation
    }
}
