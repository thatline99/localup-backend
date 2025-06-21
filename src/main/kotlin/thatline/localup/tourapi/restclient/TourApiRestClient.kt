package thatline.localup.tourapi.restclient

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.common.property.TourApiProperty
import thatline.localup.tourapi.exception.ExternalTourApiException
import thatline.localup.tourapi.response.AreaBasedListResponse
import thatline.localup.tourapi.response.LocgoRegnVisitrDDListResponse
import thatline.localup.tourapi.response.MetcoRegnVisitrDDListResponse
import thatline.localup.tourapi.response.TatsCnctrRatedListResponse
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class TourApiRestClient(
    private val tourApiProperty: TourApiProperty,
    private val restClient: RestClient,
) {
    /**
     * 한국관광공사_관광지별 연관 관광지 정보: 지역기반 관광지별 연관 관광지 정보 목록 조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param baseYm 기준 날짜 조회
     * @param areaCd 관광지 지역 코드
     * @param signguCd 관광지 시군구 코드
     * @return [AreaBasedListResponse]
     *
     * @see <a href="https://www.data.go.kr/data/15128560/openapi.do">공공데이터포털 API 문서</a>
     */
    fun areaBasedList(
        pageNo: Long,
        numOfRows: Long,
        baseYm: String,
        areaCd: String,
        signguCd: String,
    ): AreaBasedListResponse {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.tarRlteTarService.firstPath}${tourApiProperty.tarRlteTarService.areaBasedList.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.tarRlteTarService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("baseYm", baseYm)
            .queryParam("areaCd", areaCd)
            .queryParam("signguCd", signguCd)
            .queryParam("_type", tourApiProperty.tarRlteTarService.areaBasedList.responseType)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, AreaBasedListResponse::class.java)

        return response
    }

    /**
     * 한국관광공사_관광지 집중률 방문자 추이 예측 정보: 관광지 집중률 정보 목록조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param areaCd 관광지 지역 코드
     * @param signguCd 관광지 시군구 코드
     * @param tAtsNm 관광지명
     * @return
     *
     * @see <a href="https://www.data.go.kr/data/15128555/openapi.do">공공데이터포털 API 문서</a>
     */
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

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 관광역 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @property pageNo 페이지 번호
     * @property numOfRows 한 페이지 결과 수
     * @property startYmd 시작 연월일
     * @property endYmd 종료 연월일
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
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

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @property pageNo 페이지 번호
     * @property numOfRows 한 페이지 결과 수
     * @property startYmd 시작 연월일
     * @property endYmd 종료 연월일
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
    fun locgoRegnVisitrDDList(
        pageNo: Long,
        numOfRows: Long,
        startYmd: String,
        endYmd: String,
    ): LocgoRegnVisitrDDListResponse {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.dataLabService.firstPath}${tourApiProperty.dataLabService.locgoRegnVisitrDDList.secondPath}"
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

        return retrieveTourApi(uri, LocgoRegnVisitrDDListResponse::class.java)
    }

    private fun <T> retrieveTourApi(
        uri: URI,
        responseType: Class<T>,
    ): T {
        try {
            return restClient.get()
                .uri(uri)
                .retrieve()
                .body(responseType)
                ?: throw ExternalTourApiException(cause = null)
        } catch (exception: RestClientException) {
            throw ExternalTourApiException(cause = exception)
        }
    }
}
