package thatline.localup.tourapi.restclient

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.common.annotation.ApiWindow
import thatline.localup.common.annotation.OpenApiQuota
import thatline.localup.common.property.TourApiProperty
import thatline.localup.common.util.queryParamIfNotNull
import thatline.localup.tourapi.exception.TourApiException
import thatline.localup.tourapi.exception.TourApiKorService2AreaBasedList2Exception
import thatline.localup.tourapi.response.*
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class TourApiRestClient(
    private val tourApiProperty: TourApiProperty,
    private val restClient: RestClient,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 지역코드조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param areaCode 시도 코드 (선택)
     * @return [AreaCode2Response]
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "korService2:areaCode2", limit = 1000, ApiWindow.DAILY)
    fun korServiceAreaCode2(
        pageNo: Long,
        numOfRows: Long,
        areaCode: String? = null,
    ): AreaCode2Response {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.korService2.firstPath}${tourApiProperty.korService2.areaCode2.secondPath}"
        )

        val builder = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.korService2.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParamIfNotNull("areaCode", areaCode)
            .queryParam("_type", tourApiProperty.korService2.areaCode2.responseType)

        val uri = builder.build(true).toUri()

        val response = retrieveTourApi(uri, AreaCode2Response::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 법정동코드조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param lDongRegnCd 법정동 시도코드 (선택)
     * @return [LdongCode2Response]
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "korService2:ldongCode2", limit = 1000, ApiWindow.DAILY)
    fun korService2LdongCode2(
        pageNo: Long,
        numOfRows: Long,
        lDongRegnCd: String? = null,
    ): LdongCode2Response {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.korService2.firstPath}${tourApiProperty.korService2.ldongCode2.secondPath}"
        )

        val builder = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.korService2.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("_type", tourApiProperty.korService2.ldongCode2.responseType)
            .queryParamIfNotNull("lDongRegnCd", lDongRegnCd)

        val uri = builder.build(true).toUri()

        val response = retrieveTourApi(uri, LdongCode2Response::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 지역 기반 관광 정보 조회
     *
     * @param pageNo 페이지 번호 (선택)
     * @param numOfRows 한 페이지 결과 수 (선택)
     * @param lDongRegnCd 법정동 시도 코드 (선택)
     * @param lDongSignguCd 법정동 시군구 코드 (선택)
     * @param lclsSystm1 분류 체계 대분류 (선택)
     * @param lclsSystm2 분류 체계 중분류 (선택)
     * @param lclsSystm3 분류 체계 소분류 (선택)
     * @return [KorService2AreaBasedList2Response]
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "korService2:areaBasedList2", limit = 1000, ApiWindow.DAILY)
    fun korService2AreaBasedList2(
        pageNo: Long? = null,
        numOfRows: Long? = null,
        lDongRegnCd: String? = null,
        lDongSignguCd: String? = null,
        lclsSystm1: String? = null,
        lclsSystm2: String? = null,
        lclsSystm3: String? = null,
    ): KorService2AreaBasedList2Response {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.korService2.firstPath}${tourApiProperty.korService2.areaBasedList2.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.korService2.serviceKey)
            .queryParamIfNotNull("pageNo", pageNo)
            .queryParamIfNotNull("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("_type", "JSON")
            .queryParam("arrange", KorService2Arrange.TITLE.code)
            .queryParamIfNotNull("lDongRegnCd", lDongRegnCd)
            .queryParamIfNotNull("lDongSignguCd", lDongSignguCd)
            .queryParamIfNotNull("lclsSystm1", lclsSystm1)
            .queryParamIfNotNull("lclsSystm2", lclsSystm2)
            .queryParamIfNotNull("lclsSystm3", lclsSystm3)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, KorService2AreaBasedList2Response::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 행사 정보 조회
     *
     * @param pageNo 페이지 번호 (선택)
     * @param numOfRows 한 페이지 결과 수 (선택)
     * @param eventStartDate 행사 시작일
     * @param eventEndDate 행사 종료일 (선택)
     * @param lDongRegnCd 법정동 시도 코드 (선택)
     * @param lDongSignguCd 법정동 시군구 코드 (선택)
     * @return [KorService2SearchFestival2Response]
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "korService2:searchFestival2", limit = 1000, ApiWindow.DAILY)
    fun korService2SearchFestival2(
        pageNo: Long? = null,
        numOfRows: Long? = null,
        eventStartDate: String,
        eventEndDate: String? = null,
        lDongRegnCd: String? = null,
        lDongSignguCd: String? = null,
    ): KorService2SearchFestival2Response {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.korService2.firstPath}${tourApiProperty.korService2.searchFestival2.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.korService2.serviceKey)
            .queryParamIfNotNull("pageNo", pageNo)
            .queryParamIfNotNull("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("_type", "JSON")
            .queryParam("arrange", KorService2Arrange.TITLE.code)
            .queryParam("eventStartDate", eventStartDate)
            .queryParamIfNotNull("eventEndDate", eventEndDate)
            .queryParamIfNotNull("lDongRegnCd", lDongRegnCd)
            .queryParamIfNotNull("lDongSignguCd", lDongSignguCd)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, KorService2SearchFestival2Response::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_국문 관광정보 서비스_GW: 소개 정보 조회 15 (축제/공연/행사)
     *
     * @param contentId 콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 ID, 15로 고정
     * @param pageNo 페이지 번호 (선택)
     * @param numOfRows 한 페이지 결과 수 (선택)
     * @return [KorService2DetailIntro215Response]
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "korService2:detailIntro215", limit = 1000, ApiWindow.DAILY)
    fun korService2DetailIntro215(
        contentId: String,
        contentTypeId: String = "15",
        pageNo: Long? = null,
        numOfRows: Long? = null,
    ): KorService2DetailIntro215Response {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.korService2.firstPath}${tourApiProperty.korService2.detailIntro2.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.korService2.serviceKey)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("_type", "JSON")
            .queryParam("contentId", contentId)
            .queryParam("contentTypeId", contentTypeId)
            .queryParamIfNotNull("pageNo", pageNo)
            .queryParamIfNotNull("numOfRows", numOfRows)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, KorService2DetailIntro215Response::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

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
    @OpenApiQuota(name = "tarRlteTarService1:areaBasedList", limit = 1000, ApiWindow.DAILY)
    fun tarRlteTarService1AreaBasedList(
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

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_기초지자체 중심 관광지 정보: 지역기반 중심 관광지 정보 목록 조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param baseYm 기준 날짜 조회
     * @param areaCd 중심지 지역 코드
     * @param signguCd 중심지 시군구 코드
     * @return [AreaBasedListResponse2]
     *
     * @see <a href="https://www.data.go.kr/data/15128559/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "locgoHubTarService1:areaBasedList2", limit = 1000, ApiWindow.DAILY)
    fun locgoHubTarService1AreaBasedList2(
        pageNo: Long,
        numOfRows: Long,
        baseYm: String,
        areaCd: String,
        signguCd: String,
    ): AreaBasedListResponse2 {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.locgoHubTarService.firstPath}${tourApiProperty.locgoHubTarService.areaBasedList.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.locgoHubTarService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("baseYm", baseYm)
            .queryParam("areaCd", areaCd)
            .queryParam("signguCd", signguCd)
            .queryParam("_type", tourApiProperty.locgoHubTarService.areaBasedList.responseType)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, AreaBasedListResponse2::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

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
     * @return [TatsCnctrRatedListResponse]
     *
     * @see <a href="https://www.data.go.kr/data/15128555/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "tatsCnctrRateService:tatsCnctrRatedList", limit = 1000, ApiWindow.DAILY)
    fun tatsCnctrRateServiceTatsCnctrRatedList(
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

        val response = retrieveTourApi(uri, TatsCnctrRatedListResponse::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param startYmd 시작 연월일
     * @param endYmd 종료 연월일
     * @return [MetcoRegnVisitrDDListResponse]
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "dataLabService:metcoRegnVisitrDDList", limit = 1000, ApiWindow.DAILY)
    fun dataLabServiceMetcoRegnVisitrDDList(
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

        val response = retrieveTourApi(uri, MetcoRegnVisitrDDListResponse::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    /**
     * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
     *
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param startYmd 시작 연월일
     * @param endYmd 종료 연월일
     * @return [LocgoRegnVisitrDDListResponse]
     *
     * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
     */
    @OpenApiQuota(name = "dataLabService:locgoRegnVisitrDDList", limit = 1000, ApiWindow.DAILY)
    fun dataLabServiceLocgoRegnVisitrDDList(
        pageNo: Long?,
        numOfRows: Long?,
        startYmd: String,
        endYmd: String,
    ): LocgoRegnVisitrDDListResponse {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.dataLabService.firstPath}${tourApiProperty.dataLabService.locgoRegnVisitrDDList.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.dataLabService.serviceKey)
            .queryParamIfNotNull("pageNo", pageNo)
            .queryParamIfNotNull("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("startYmd", startYmd)
            .queryParam("endYmd", endYmd)
            .queryParam("_type", "JSON")
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, LocgoRegnVisitrDDListResponse::class.java)

        val responseHeader = response.response.header

        if (responseHeader.resultCode != "0000") {
            throw TourApiKorService2AreaBasedList2Exception(
                failedUri = uri.toString(),
                resultCode = responseHeader.resultCode,
                resultMessage = responseHeader.resultMsg
            )
        }

        return response
    }

    private fun <T> retrieveTourApi(
        uri: URI,
        responseType: Class<T>,
    ): T {
        try {
            logger.debug("URI: {}", uri.toString())

            return restClient.get()
                .uri(uri)
                .retrieve()
                .body(responseType)
                ?: throw TourApiException(
                    failedUri = uri.toString()
                )
        } catch (exception: RestClientException) {
            throw TourApiException(
                failedUri = uri.toString(),
                cause = exception
            )
        }
    }
}
