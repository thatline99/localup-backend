package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.constant.dto.TourApiArea
import thatline.localup.dto.localup.AreaVisitors
import thatline.localup.dto.localup.SignguStatistics
import thatline.localup.request.LuLocgoRegnVisitrDDListRequest
import thatline.localup.request.LuMetcoRegnVisitrDDListRequest
import thatline.localup.request.SearchTouristAttractionConcentrationLast30DaysRequest
import thatline.localup.request.SearchTouristAttractionRequest
import thatline.localup.response.BaseResponse
import thatline.localup.response.dto.TouristAttractionConcentrationRateLast30Days
import thatline.localup.service.LocalUpService
import thatline.localup.service.LuLocgoRegnVisitrDDListService
import thatline.localup.service.LuMetcoRegnVisitrDDListService

// TODO: noah, API 경로 및 메서드명 합의 필요

@RestController
@RequestMapping("/api/local-up")
class LocalUpController(
    private val localUpService: LocalUpService,
    private val luMetcoRegnVisitrDDListService: LuMetcoRegnVisitrDDListService,
    private val luLocgoRegnVisitrDDListService: LuLocgoRegnVisitrDDListService,
) {
    // 한국관광공사_TourAPI_관광지_시군구_코드정보 조회
    @GetMapping("/tour-api-areas")
    fun getTourApiAreas(): ResponseEntity<BaseResponse<List<TourApiArea>>> {
        val tourApiAreas = localUpService.getTourApiAreas()

        return ResponseEntity.ok(BaseResponse.success(tourApiAreas))
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd와 signguCd로 관광지 목록 조회
    @GetMapping("/tatsCnctrRatedList/tourist-attractions")
    fun searchTouristAttractions(
        @RequestBody request: SearchTouristAttractionRequest,
    ): ResponseEntity<BaseResponse<List<String>>> {
        val touristAttractions = localUpService.searchTouristAttractions(request.areaCd, request.signguCd)

        return ResponseEntity.ok(BaseResponse.success(touristAttractions))
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // 최근 30일간의 관광지 집중률 데이터 조회
    // TODO: noah, 캐시 필요
    @GetMapping("/tatsCnctrRatedList/tourist-attraction-last-30-days")
    fun searchTouristAttractionConcentrationLast30Days(
        @RequestBody request: SearchTouristAttractionConcentrationLast30DaysRequest,
    ): ResponseEntity<BaseResponse<TouristAttractionConcentrationRateLast30Days>> {
        val touristAttractionConcentrationRateLast30Days =
            localUpService.searchTouristAttractionConcentrationLast30Days(
                request.areaCd,
                request.signguCd,
                request.tatsNm
            )

        return ResponseEntity.ok(BaseResponse.success(touristAttractionConcentrationRateLast30Days))
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * link: https://www.data.go.kr/data/15101972/openapi.do
     *
     * @author [noah (조태현)]
     */
    @GetMapping("/metcoRegnVisitrDDList")
    fun metcoRegnVisitrDDList(
        request: LuMetcoRegnVisitrDDListRequest,
    ): ResponseEntity<BaseResponse<List<AreaVisitors>>> {
        val result = luMetcoRegnVisitrDDListService.searchData(
            request.startDate,
            request.endDate,
            request.areaCode,
        )

        return ResponseEntity.ok(BaseResponse.success(result))
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * link: https://www.data.go.kr/data/15101972/openapi.do
     *
     * @author [noah (조태현)]
     */
    @GetMapping("/locgoRegnVisitrDDList")
    fun locgoRegnVisitrDDList(
        request: LuLocgoRegnVisitrDDListRequest,
    ): ResponseEntity<BaseResponse<SignguStatistics>> {
        val result = luLocgoRegnVisitrDDListService.searchData(
            request.startDate,
            request.endDate,
            request.signguCode,
        )

        return if (result == null) {
            ResponseEntity.status(404).body(BaseResponse.failure())
        } else {
            ResponseEntity.ok(BaseResponse.success(result))
        }
    }
}
