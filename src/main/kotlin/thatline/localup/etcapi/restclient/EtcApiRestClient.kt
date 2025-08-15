package thatline.localup.etcapi.restclient

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.common.property.EtcApiProperty
import thatline.localup.etcapi.exception.ExternalEtcApiException
import thatline.localup.etcapi.response.GetFcstVersionResponse
import thatline.localup.etcapi.response.GetUltraSrtNcstResponse
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
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지에 포함할 결과 수
     * @param baseDate 발표 일자
     * @param baseTime 발표 시각
     * @param nx 예보 지점의 X 좌표
     * @param ny 예보 지점의 Y 좌표
     * @return [GetUltraSrtNcstResponse]
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
            .queryParam("dataType", etcApiProperty.kma.vilageFcstInfoService.getUltraSrtNcst.dataType)
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
     * @param pageNo 페이지 번호
     * @param numOfRows 한 페이지 결과 수
     * @param fileType 파일 구분
     * @param basedDatetime 발표일시분
     * @return [GetFcstVersionResponse]
     *
     * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
     */
    fun getFcstVersion(
        pageNo: Long,
        numOfRows: Long,
        fileType: String,
        basedDatetime: String,
    ): GetFcstVersionResponse {
        val fromUri = URI.create(
            "${etcApiProperty.kma.baseUrl}${etcApiProperty.kma.vilageFcstInfoService.firstPath}${etcApiProperty.kma.vilageFcstInfoService.getFcstVersion.secondPath}"
        )

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", etcApiProperty.kma.vilageFcstInfoService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("dataType", etcApiProperty.kma.vilageFcstInfoService.getFcstVersion.dataType)
            .queryParam("ftype", fileType)
            .queryParam("basedatetime", basedDatetime)
            .build(true)
            .toUri()

        val response = retrieveTourApi(uri, GetFcstVersionResponse::class.java)

        if (response.response.header.resultCode != "00") {
            log.warn(
                "resultCode={}, resultMsg={}",
                response.response.header.resultCode,
                response.response.header.resultMsg
            )
        }

        return response
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
                ?: throw ExternalEtcApiException()
        } catch (exception: RestClientException) {
            throw ExternalEtcApiException(cause = exception)
        }
    }
}
