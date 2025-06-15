package thatline.localup.service

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import thatline.localup.constant.TourApi
import thatline.localup.constant.dto.TourApiArea
import thatline.localup.dto.localup.MetcoAreaVisitors
import thatline.localup.dto.localup.MetcoVisitorItem
import thatline.localup.dto.localup.MetcoVisitorsByDate
import thatline.localup.response.dto.TouristAttractionConcentrationRateLast30Days
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        val totalCount = tourApiService.tatsCnctrRatedList(1, 1, areaCd, signguCd, "")
            .response.body.totalCount

        return tourApiService.tatsCnctrRatedList(1, totalCount, areaCd, signguCd, "")
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
        val response = tourApiService.tatsCnctrRatedList(1, 30, areaCd, signguCd, tatsNm)

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

    fun searchAreaWithMetcoRegnVisitrDDList() {
        val totalCount = tourApiService.metcoRegnVisitrDDList(
            1,
            1,
            "20250101",
            "20250101",
        ).response.body.totalCount

        val metcoRegnVisitrDDList = tourApiService.metcoRegnVisitrDDList(
            1,
            totalCount,
            "20250101",
            "20250101",
        )
    }

    // 한국관광공사_관광빅데이터 정보서비스_GW
    // link: https://www.data.go.kr/data/15101972/openapi.do
    fun test(
        startDate: LocalDate,
        endDate: LocalDate,
        areaName: String,
    ): List<MetcoAreaVisitors> {
        val startDate = startDate.format(DATETIME_FORMATTER)
        val endDate = endDate.format(DATETIME_FORMATTER)

        val totalCount = tourApiService.metcoRegnVisitrDDList(
            1,
            1,
            startDate,
            endDate
        ).response.body.totalCount

        val items = tourApiService.metcoRegnVisitrDDList(
            1,
            totalCount,
            startDate,
            endDate
        ).response.body.items.item

        val filteredItems = items.filter {
            it.areaNm == areaName
        }

        val areaGroups = filteredItems.groupBy { it.areaCode to it.areaNm }


        return areaGroups.map { (areaKey, areaItems) ->
            val dateGroups = areaItems.groupBy { it.baseYmd }

            val visitorsByDate = dateGroups.map { (rawDate, itemsOnDate) ->
                val formattedDate = LocalDate.parse(rawDate, DateTimeFormatter.BASIC_ISO_DATE)
                    .format(DateTimeFormatter.ISO_DATE)

                val visitors = itemsOnDate.map {
                    MetcoVisitorItem(
                        touDivCd = it.touDivCd,
                        touNum = it.touNum.toDouble()
                    )
                }

                MetcoVisitorsByDate(
                    date = formattedDate,
                    visitors = visitors
                )
            }.sortedBy { it.date }

            MetcoAreaVisitors(
                areaCode = areaKey.first,
                areaName = areaKey.second,
                visitorsByDate = visitorsByDate
            )
        }
    }

    companion object {
        private val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
