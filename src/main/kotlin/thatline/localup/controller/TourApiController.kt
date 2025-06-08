package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.dto.tourApi.MetcoRegnVisitrDDListResponse
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse
import thatline.localup.exception.ExternalTourApiException
import thatline.localup.request.MetcoRegnVisitrDDListRequest
import thatline.localup.request.TatsCnctrRatedListRequest
import thatline.localup.service.TourApiService

@RestController
@RequestMapping("/api/tour-api")
class TourApiController(
    private val tourApiService: TourApiService,
) {
    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보
    // link: https://www.data.go.kr/data/15128555/openapi.do
    // 관광지 집중률 정보 목록조회
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

    // 한국관광공사_관광빅데이터 정보서비스_GW
    // link: https://www.data.go.kr/data/15101972/openapi.do
    // 광역 지자체 지역방문자수 집계 데이터 정보 조회
    @GetMapping("/metcoRegnVisitrDDList")
    fun getMetcoRegnVisitrDDList(
        request: MetcoRegnVisitrDDListRequest,
    ): MetcoRegnVisitrDDListResponse {
        return tourApiService.metcoRegnVisitrDDList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            startYmd = request.startYmd,
            endYmd = request.endYmd,
        )
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(ExternalTourApiException::class)
    fun handleExternalTourApi(exception: ExternalTourApiException): ResponseEntity<Void> {
        return ResponseEntity.internalServerError().build()
    }
}
