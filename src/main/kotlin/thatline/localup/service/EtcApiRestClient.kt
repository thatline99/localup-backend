package thatline.localup.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.dto.etcApi.GetUltraSrtNcstResponse
import thatline.localup.exception.ExternalTourApiException
import thatline.localup.property.EtcApiProperty
import java.net.URI

@Service
class EtcApiRestClient(
    private val etcApiProperty: EtcApiProperty,
    private val restClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회
     *
     * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
     */
    fun getUltraSrtNcst(
        pageNo: Long,
        numOfRows: Long,
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
    ): GetUltraSrtNcstResponse {
        val fromUri = URI.create(
            "${etcApiProperty.kma.baseUrl}${etcApiProperty.kma.vilageFcstInfoService.firstPath}${etcApiProperty.kma.vilageFcstInfoService.getUltraSrtNcst.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", etcApiProperty.kma.vilageFcstInfoService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("dataType", "JSON")
            .queryParam("base_date", baseDate)
            .queryParam("base_time", baseTime)
            .queryParam("nx", nx)
            .queryParam("ny", ny)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, GetUltraSrtNcstResponse::class.java)

        if (response.response.header.resultCode != "00") {
            log.warn(
                "resultCode={}, resultMsg={}",
                response.response.header.resultCode,
                response.response.header.resultMsg
            )
        }

        return response
    }

    /**
     * 기상청_단기예보 ((구) 동네예보) 조회서비스: 예보버전조회
     *
     * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
     */
    // TODO-noah: 작성

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
