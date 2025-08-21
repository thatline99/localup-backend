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

    // TODO-noah: 삭제, 아직 인증이 완료되지 않아 사용하는 코드입니다.
    @GetMapping("/test")
    fun getDashboardInformation(): ResponseEntity<BaseResponse<DashboardOverview>> {
        // 해당 user id는 로컬 db에 따라 달라질 수 있습니다.
        val dashboardOverview = dashboardFacade.getDashboardOverview("689eb417e295ca9144b875a0")

        return ResponseEntity.ok(BaseResponse.success(data = dashboardOverview))
    }
}
