package thatline.localup.dashboard.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.common.annotation.RequireUser
import thatline.localup.common.response.BaseResponse
import thatline.localup.dashboard.dto.DashboardOverview
import thatline.localup.dashboard.service.DashboardFacade

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardFacade: DashboardFacade,
) {
    @GetMapping
    @RequireUser
    fun getDashboardOverview(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<BaseResponse<DashboardOverview>> {
        val dashboardOverview = dashboardFacade.getDashboardOverview(userId)

        return ResponseEntity.ok(BaseResponse.success(data = dashboardOverview))
    }
}
