package thatline.localup.dashboard.service

import org.springframework.stereotype.Service
import thatline.localup.chatgpt.service.ChatGptService
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.dashboard.dto.DashboardOverview
import thatline.localup.etcapi.dto.ShortTermForecastInformation
import thatline.localup.etcapi.service.ForecastService
import thatline.localup.tourapi.dto.VisitorStatisticsInformation
import thatline.localup.tourapi.service.TouristAttractionService
import thatline.localup.user.service.UserService
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Service
class DashboardFacade(
    private val userService: UserService,
    private val touristAttractionService: TouristAttractionService,
    private val forecastService: ForecastService,
    private val chatGptService: ChatGptService,
) {
    // TODO-noah: 분리
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
    fun getInsight(userId: String): String {
        // 사용자 정보
        val foundUserBusinessDto = userService.findBusiness(userId)

        // 1. 오늘, 내일, 모레 날씨
        val foundShortTermForecast = findShortTermForecast(
            userId = userId,
        )

        // 2. 작년 같은 주의 전주 ~ 다음주 방문객 통계
        val (lastYearPreviousWeekMonday, lastYearNextWeekSunday) = calculateLastYearSameWeekRange()

        val foundVisitorStatistics = touristAttractionService.findVisitorStatistics(
            sigunguCode = foundUserBusinessDto.sigunguCode,
            startDate = lastYearPreviousWeekMonday,
            endDate = lastYearNextWeekSunday,
        )

        // 3. 오늘 ~ 이번 달 마지막 일 축제 조회
        val foundSigunguEvents =
            touristAttractionService.findOngoingOrUpComingSigunguEventsFromTodayToMonthEnd(
                sigunguCode = foundUserBusinessDto.sigunguCode,
            )

        val data = """
            사용자 사업 정보: $foundShortTermForecast,
            작년 같은 주의 전주 ~ 다음주 방문객 통계: $foundVisitorStatistics,
            오늘 ~ 이번 달 마지막 일 축제 조회: $foundSigunguEvents,
        """.trimIndent()

        return chatGptService.getInsight(
            data = data,
        )
    }

    // 현재 날짜 기준으로 작년 같은 주의 전주 ~ 다음주 계산
    private fun calculateLastYearSameWeekRange(): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()

        // 작년 같은 날짜
        val lastYearSameDate = today.minusYears(1)

        // 작년 같은 주의 월요일 구하기
        val lastYearSameWeekMonday = lastYearSameDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        // 전주 월요일 (작년 같은 주의 전주)
        val lastYearPreviousWeekMonday = lastYearSameWeekMonday.minusWeeks(1)

        // 다음주 일요일 (작년 같은 주의 다음주)
        val lastYearNextWeekSunday =
            lastYearSameWeekMonday.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return Pair(lastYearPreviousWeekMonday, lastYearNextWeekSunday)
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
