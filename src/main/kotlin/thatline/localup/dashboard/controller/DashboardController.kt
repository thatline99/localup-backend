package thatline.localup.dashboard.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import thatline.localup.common.annotation.RequireUser
import thatline.localup.common.response.BaseResponse
import thatline.localup.dashboard.dto.*
import thatline.localup.dashboard.request.FindVisitorStatisticsRequest
import thatline.localup.dashboard.service.AiInsightService
import thatline.localup.dashboard.service.DashboardFacade
import thatline.localup.etcapi.dto.ShortTermForecastInformation
import thatline.localup.tourapi.dto.VisitorStatisticsInformation

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardFacade: DashboardFacade,
    private val aiInsightService: AiInsightService,
) {
    // TODO-noah: 분리
    @GetMapping
    @RequireUser
    fun getDashboardOverview(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<BaseResponse<DashboardOverview>> {
        val dashboardOverview = dashboardFacade.getDashboardOverview(userId)

        return ResponseEntity.ok(BaseResponse.success(data = dashboardOverview))
    }

    @GetMapping("/short-term-forecast")
    @RequireUser
    fun findShortTermForecast(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<BaseResponse<ShortTermForecastInformation>> {
        val shortTermForecastInformation = dashboardFacade.findShortTermForecast(userId)

        return ResponseEntity.ok(BaseResponse.success(data = shortTermForecastInformation))
    }

    @GetMapping("/visitor-statistics")
    fun findVisitorStatistics(
        @AuthenticationPrincipal userId: String,
        @Valid request: FindVisitorStatisticsRequest,
    ): ResponseEntity<BaseResponse<VisitorStatisticsInformation>> {
        val visitorStatisticsInformation = dashboardFacade.findVisitorStatistics(
            userId = userId,
            startDate = request.startDate,
            endDate = request.endDate,
        )

        return ResponseEntity.ok(BaseResponse.success(data = visitorStatisticsInformation))
    }

    @PostMapping("/weather-insight")
    @RequireUser
    fun generateWeatherInsight(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: WeatherInsightRequest
    ): ResponseEntity<BaseResponse<WeatherInsightResponse>> {
        val insight = aiInsightService.generateWeatherInsight(request, userId)
        return ResponseEntity.ok(BaseResponse.success(data = insight))
    }

    @PostMapping("/business-metrics")
    @RequireUser
    fun generateBusinessMetrics(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: BusinessMetricsRequest
    ): ResponseEntity<BaseResponse<BusinessMetricsResponse>> {
        val metrics = aiInsightService.generateBusinessMetrics(request, userId)
        return ResponseEntity.ok(BaseResponse.success(data = metrics))
    }
}
