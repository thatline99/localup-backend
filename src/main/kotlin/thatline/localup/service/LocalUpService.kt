package thatline.localup.service

import org.springframework.stereotype.Service
import thatline.localup.constant.TourApi
import thatline.localup.constant.dto.TourApiArea
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse

@Service
class LocalUpService(
    private val tourApiService: TourApiService,
) {
    // 한국관광공사_TourAPI_관광지_시군구_코드정보 조회
    fun getTourApiAreas(): List<TourApiArea> {
        return TourApi.areas
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd와 signguCd로 관광지 목록 조회
    fun searchTouristAttractions(areaCd: String, signguCd: String): List<String> {
        // 1. total Count 조회
        val tatsCnctrRatedListResponse1 = tourApiService.tatsCnctrRatedList(1, 1, areaCd, signguCd, "")

        val totalCount = tatsCnctrRatedListResponse1.response.body.totalCount

        // 2. 관광지 목록 조회
        val tatsCnctrRatedListResponse2 = tourApiService.tatsCnctrRatedList(1, totalCount, areaCd, signguCd, "")

        return tatsCnctrRatedListResponse2.response.body.items.item
            .map { it.tAtsNm }
            .distinct()
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd, signguCd 그리고 tatsNm로 관광지 목록 조회
    // TODO: noah, TatsCnctrRatedListResponse 대신 DTO 추가
    fun searchTouristAttractionConcentrationLast30Days(
        areaCd: String,
        signguCd: String,
        tatsNm: String,
    ): TatsCnctrRatedListResponse {
        return tourApiService.tatsCnctrRatedList(1, 30, areaCd, signguCd, tatsNm)
    }
}
