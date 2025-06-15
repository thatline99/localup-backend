package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import thatline.localup.constant.dto.TourApiArea
import thatline.localup.dto.localup.MetcoAreaVisitors
import thatline.localup.request.SearchTouristAttractionConcentrationLast30DaysRequest
import thatline.localup.request.SearchTouristAttractionRequest
import thatline.localup.response.BaseResponse
import thatline.localup.response.dto.TouristAttractionConcentrationRateLast30Days
import thatline.localup.service.LocalUpService
import java.time.LocalDate

// TODO: noah, API 경로 및 메서드명 합의 필요

@RestController
@RequestMapping("/api/local-up")
class LocalUpController(
    private val localUpService: LocalUpService,
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

    @GetMapping("/test")
    fun test(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        @RequestParam areaName: String, // 서울특별시
    ): List<MetcoAreaVisitors> {
        val result = localUpService.test(
            startDate,
            endDate,
            areaName
        )

        return result
    }
}
