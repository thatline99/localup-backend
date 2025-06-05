package thatline.localup.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import thatline.localup.dto.tourApi.TatsCnctrRatedResponse
import thatline.localup.service.TourApiService

@RestController
@RequestMapping("/api/tour-api")
class TourApiController(
    private val tourApiService: TourApiService,
) {
    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    @GetMapping("/tatsCnctrRatedList")
    fun getTatsCnctrRatedList(
        @RequestParam areaCd: String,
        @RequestParam signguCd: String,
        @RequestParam(required = false, defaultValue = "") tAtsNm: String,
        @RequestParam(defaultValue = "1") pageNo: Int,
        @RequestParam(defaultValue = "10") numOfRows: Int,
    ): TatsCnctrRatedResponse? {
        return tourApiService.tatsCnctrRatedList(
            pageNo = pageNo,
            numOfRows = numOfRows,
            areaCd = areaCd,
            signguCd = signguCd,
            tAtsNm = tAtsNm
        )
    }
}
