package thatline.localup.localup.service

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import thatline.localup.common.constant.TourApi
import thatline.localup.common.constant.dto.TourApiArea
import thatline.localup.localup.response.dto.Area
import thatline.localup.localup.response.dto.HourlyWeatherInformation
import thatline.localup.localup.response.dto.Sigungu
import thatline.localup.localup.response.dto.TouristAttractionConcentrationRateLast30Days
import thatline.localup.tourapi.restclient.TourApiRestClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class LocalUpFacade(
    private val tourApiRestClient: TourApiRestClient,
    private val luCommonService: LuCommonService,
    private val luWeatherService: LuWeatherService,
) {
    // 한국관광공사_TourAPI_관광지_시군구_코드정보 조회
    fun getTourApiAreas(): List<TourApiArea> {
        return TourApi.areas
    }

    // 1. LuCommonService

    fun searchAreas(): List<Area> {
        return luCommonService.searchAreas()
    }

    fun searchSigungus(
        areaCode: String,
    ): List<Sigungu> {
        return luCommonService.searchSigungus(areaCode)
    }

    // 2. LuWeatherService

    fun getHourlyWeatherInformationByCoordinates(
        latitude: Double,
        longitude: Double,
    ): HourlyWeatherInformation {
        return luWeatherService.getHourlyWeatherInformationByCoordinates(latitude, longitude)
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd와 signguCd로 관광지 목록 조회
    // 1. searchTouristAttractions()에 @Cacheable 추가
    @Cacheable(cacheNames = ["touristAttractions"], key = "#areaCd + '_' + #signguCd")
    fun searchTouristAttractions(areaCd: String, signguCd: String): List<String> =
        fetchTouristAttractions(areaCd, signguCd)

    // 2. 캐시 갱신용 메서드 @CachePut
    @CachePut(cacheNames = ["touristAttractions"], key = "#areaCd + '_' + #signguCd")
    fun refreshTouristAttractions(areaCd: String, signguCd: String): List<String> =
        fetchTouristAttractions(areaCd, signguCd)

    // 3. 0시 정각 일괄 갱신 스케줄러
    @Scheduled(cron = "0 0 0 * * *")
    fun refreshAllTouristAttractions() {
        TourApi.areas.forEach { area ->
            refreshTouristAttractions(area.areaCd, area.sigunguCd)
        }
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // areaCd와 signguCd로 관광지 목록 조회
    private fun fetchTouristAttractions(areaCd: String, signguCd: String): List<String> {
        val totalCount = tourApiRestClient.tatsCnctrRatedList(1, 1, areaCd, signguCd, "")
            .response.body.totalCount

        return tourApiRestClient.tatsCnctrRatedList(1, totalCount, areaCd, signguCd, "")
            .response.body.items.item
            .map { it.tAtsNm }
            .distinct()
    }

    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보, 관광지 집중률 정보 목록조회
    // 최근 30일간의 관광지 집중률 데이터 조회
    fun searchTouristAttractionConcentrationLast30Days(
        areaCd: String,
        signguCd: String,
        tatsNm: String,
    ): TouristAttractionConcentrationRateLast30Days {
        val response = tourApiRestClient.tatsCnctrRatedList(1, 30, areaCd, signguCd, tatsNm)

        val items = response.response.body.items.item

        val firstItem = items.first()

        val concentrationRates = items.map {
            TouristAttractionConcentrationRateLast30Days.ConcentrationRate(
                date = LocalDate.parse(it.baseYmd, DateTimeFormatter.BASIC_ISO_DATE),
                rate = it.cnctrRate
            )
        }

        return TouristAttractionConcentrationRateLast30Days(
            areaCode = firstItem.areaCd,
            areaName = firstItem.areaNm,
            signguCode = firstItem.signguCd,
            signguName = firstItem.signguNm,
            concentrationRates = concentrationRates
        )
    }
}
