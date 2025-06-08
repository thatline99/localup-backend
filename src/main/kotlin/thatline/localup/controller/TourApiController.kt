package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse
import thatline.localup.exception.ExternalTourApiException
import thatline.localup.request.TatsCnctrRatedListRequest
import thatline.localup.service.TourApiService

@RestController
@RequestMapping("/api/tour-api")
class TourApiController(
    private val tourApiService: TourApiService,
) {
    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // link: https://www.data.go.kr/data/15128555/openapi.do
    @GetMapping("/tatsCnctrRatedList")
    fun getTatsCnctrRatedList(
        request: TatsCnctrRatedListRequest,
    ): TatsCnctrRatedListResponse {
        return tourApiService.tatsCnctrRatedList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            areaCd = request.areaCd,
            signguCd = request.signguCd,
            tAtsNm = request.tAtsNm
        )
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(ExternalTourApiException::class)
    fun handleExternalTourApi(exception: ExternalTourApiException): ResponseEntity<Void> {
        return ResponseEntity.internalServerError().build()
    }
}
