package thatline.localup.tourapi.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.tourapi.exception.ExternalTourApiException
import thatline.localup.tourapi.request.*
import thatline.localup.tourapi.response.*
import thatline.localup.tourapi.restclient.TourApiRestClient

@RestController
@RequestMapping("/api/tour-api")
class TourApiController(
    private val tourApiRestClient: TourApiRestClient,
) {
    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 지역코드조회
     *
     * @param request [AreaCode2Request]
     * @return [ResponseEntity]<[AreaCode2Request]>
     */
    @GetMapping("/areaCode2")
    fun areaCode2(
        request: AreaCode2Request,
    ): ResponseEntity<AreaCode2Response> {
        val response = tourApiRestClient.areaCode2(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            areaCode = request.areaCode,
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 한국관광공사_관광지별 연관 관광지 정보: 지역기반 관광지별 연관 관광지 정보 목록 조회
     *
     * @param request [AreaBasedListRequest]
     * @return [ResponseEntity]<[AreaBasedListResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15128560/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/areaBasedList")
    fun areaBasedList(
        request: AreaBasedListRequest,
    ): ResponseEntity<AreaBasedListResponse> {
        val response = tourApiRestClient.areaBasedList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            baseYm = request.baseYm,
            areaCd = request.areaCd,
            signguCd = request.signguCd,
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 한국관광공사_기초지자체 중심 관광지 정보: 지역기반 중심 관광지 정보 목록 조회
     *
     * @param request [AreaBasedListRequest2]
     * @return [ResponseEntity]<[AreaBasedListResponse2]>
     *
     * @see <a href="https://www.data.go.kr/data/15128559/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/areaBasedList2")
    fun areaBasedList2(
        request: AreaBasedListRequest2,
    ): ResponseEntity<AreaBasedListResponse2> {
        val response = tourApiRestClient.areaBasedList2(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            baseYm = request.baseYm,
            areaCd = request.areaCd,
            signguCd = request.signguCd,
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 한국관광공사_관광지 집중률 방문자 추이 예측 정보: 관광지 집중률 정보 목록조회
     *
     * @param request [TatsCnctrRatedListRequest]
     * @return [ResponseEntity]<[TatsCnctrRatedListResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15128555/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/tatsCnctrRatedList")
    fun tatsCnctrRatedList(
        request: TatsCnctrRatedListRequest,
    ): ResponseEntity<TatsCnctrRatedListResponse> {
        val response = tourApiRestClient.tatsCnctrRatedList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            areaCd = request.areaCd,
            signguCd = request.signguCd,
            tAtsNm = request.tAtsNm
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @param request [MetcoRegnVisitrDDListRequest]
     * @return [ResponseEntity]<[MetcoRegnVisitrDDListResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/metcoRegnVisitrDDList")
    fun metcoRegnVisitrDDList(
        request: MetcoRegnVisitrDDListRequest,
    ): ResponseEntity<MetcoRegnVisitrDDListResponse> {
        val response = tourApiRestClient.metcoRegnVisitrDDList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            startYmd = request.startYmd,
            endYmd = request.endYmd,
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @param request [LocgoRegnVisitrDDListRequest]
     * @return [ResponseEntity]<[LocgoRegnVisitrDDListResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/locgoRegnVisitrDDList")
    fun locgoRegnVisitrDDList(
        request: LocgoRegnVisitrDDListRequest,
    ): ResponseEntity<LocgoRegnVisitrDDListResponse> {
        val response = tourApiRestClient.locgoRegnVisitrDDList(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            startYmd = request.startYmd,
            endYmd = request.endYmd,
        )

        return ResponseEntity.ok(response)
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(ExternalTourApiException::class)
    fun handleExternalTourApi(exception: ExternalTourApiException): ResponseEntity<Void> {
        return ResponseEntity.internalServerError().build()
    }
}
