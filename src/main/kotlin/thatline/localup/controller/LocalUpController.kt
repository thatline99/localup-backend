package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.constant.dto.TourApiArea
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse
import thatline.localup.request.SearchTouristAttractionConcentrationLast30DaysRequest
import thatline.localup.request.SearchTouristAttractionRequest
import thatline.localup.service.LocalUpService

// TODO: noah, API 경로 및 메서드명 합의 필요
// TODO: noah, Base 응답 필요

@RestController
@RequestMapping("/api/local-up")
class LocalUpController(
    private val localUpService: LocalUpService,
) {
    // 한국관광공사_TourAPI_관광지_시군구_코드정보 조회
    @GetMapping("/tour-api-areas")
    fun getTourApiAreas(): ResponseEntity<List<TourApiArea>> {
        val tourApiAreas = localUpService.getTourApiAreas()

        return ResponseEntity.ok(tourApiAreas)
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd와 signguCd로 관광지 목록 조회
    // TODO: noah, 캐시 필요
    @GetMapping("/tatsCnctrRatedList/tourist-attractions")
    fun searchTouristAttractions(@RequestBody request: SearchTouristAttractionRequest): ResponseEntity<List<String>> {
        println(request.areaCd + " " + request.signguCd)

        val touristAttractions = localUpService.searchTouristAttractions(request.areaCd, request.signguCd)

        return ResponseEntity.ok(touristAttractions)
    }

    @GetMapping("/tatsCnctrRatedList/tourist-attraction-last-30-days")
    fun searchTouristAttractionConcentrationLast30Days(
        @RequestBody request: SearchTouristAttractionConcentrationLast30DaysRequest,
    ): ResponseEntity<TatsCnctrRatedListResponse> {
        val tatsCnctrRatedListResponse =
            localUpService.searchTouristAttractionConcentrationLast30Days(
                request.areaCd,
                request.signguCd,
                request.tatsNm
            )

        return ResponseEntity.ok(tatsCnctrRatedListResponse)
    }
}
