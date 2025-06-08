package thatline.localup.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.dto.tourApi.MetcoRegnVisitrDDListResponse
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse
import thatline.localup.exception.ExternalTourApiException
import thatline.localup.property.TourApiProperty
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class TourApiService(
    private val tourApiProperty: TourApiProperty,
    private val restClient: RestClient,
) {
    // 한국관광공사_관광지 집중률 방문자 추이 예측 정보
    // link: https://www.data.go.kr/data/15128555/openapi.do
    // 관광지 집중률 정보 목록조회
    fun tatsCnctrRatedList(
        pageNo: Long,
        numOfRows: Long,
        areaCd: String,
        signguCd: String,
        tAtsNm: String,
    ): TatsCnctrRatedListResponse {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.tatsCnctrRateService.firstPath}${tourApiProperty.tatsCnctrRateService.tatsCnctrRatedList.secondPath}"
        )

        val encodedTAtsNm = URLEncoder.encode(tAtsNm, StandardCharsets.UTF_8)

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.tatsCnctrRateService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("areaCd", areaCd)
            .queryParam("signguCd", signguCd)
            .queryParam("tAtsNm", encodedTAtsNm)
            .queryParam("_type", "JSON")
            .build(true)
            .toUri()

        return retrieveTourApi(uri, TatsCnctrRatedListResponse::class.java)
    }

    // 한국관광공사_관광빅데이터 정보서비스_GW
    // link: https://www.data.go.kr/data/15101972/openapi.do
    // 광역 지자체 지역방문자수 집계 데이터 정보 조회
    fun metcoRegnVisitrDDList(
        pageNo: Long,
        numOfRows: Long,
        startYmd: String,
        endYmd: String,
    ): MetcoRegnVisitrDDListResponse {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.dataLabService.firstPath}${tourApiProperty.dataLabService.metcoRegnVisitrDDList.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.dataLabService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("startYmd", startYmd)
            .queryParam("endYmd", endYmd)
            .queryParam("_type", "JSON")
            .build(true)
            .toUri()

        return retrieveTourApi(uri, MetcoRegnVisitrDDListResponse::class.java)
    }

    // 한국관광공사_관광빅데이터 정보서비스_GW
    // link: https://www.data.go.kr/data/15101972/openapi.do
    // 기초 지자체 지역방문자수 집계 데이터 정보 조회

    private fun <T> retrieveTourApi(
        uri: URI,
        responseType: Class<T>,
    ): T {
        try {
            return restClient.get()
                .uri(uri)
                .retrieve()
                .body(responseType)
                ?: throw ExternalTourApiException()
        } catch (exception: RestClientException) {
            throw ExternalTourApiException()
        }
    }
}
