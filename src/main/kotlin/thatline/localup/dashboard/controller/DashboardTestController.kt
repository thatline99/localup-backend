package thatline.localup.dashboard.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.common.response.BaseResponse
import thatline.localup.dashboard.service.DashboardFacade
import thatline.localup.tourapi.dto.SigunguEventWithDates

@RestController
@RequestMapping("/api/dashboard/test")
class DashboardTestController(
    private val dashboardFacade: DashboardFacade,
) {
    @GetMapping("/events/main")
    fun findMainEvent(
        request: FindMainEventRequest,
    ): ResponseEntity<BaseResponse<SigunguEventWithDates>> {
        val foundMainEvent = dashboardFacade.findMainEvent(
            legalDongSigunguCode = request.legalDongSigunguCode
        )

        return ResponseEntity.ok(BaseResponse.success(data = foundMainEvent))
    }
}

data class FindMainEventRequest(
    val legalDongSigunguCode: String,
)
