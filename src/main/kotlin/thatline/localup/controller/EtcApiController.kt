package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.dto.etcApi.GetFcstVersionResponse
import thatline.localup.dto.etcApi.GetUltraSrtNcstResponse
import thatline.localup.request.GetFcstVersionRequest
import thatline.localup.request.GetUltraSrtNcstRequest
import thatline.localup.service.EtcApiRestClient

@RestController
@RequestMapping("/api/etc-api")
class EtcApiController(
    private val etcApiRestClient: EtcApiRestClient,
) {
    /**
     * 기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회
     *
     * @param request [GetUltraSrtNcstRequest]
     * @return [ResponseEntity]<[GetUltraSrtNcstResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/getUltraSrtNcst")
    fun getUltraSrtNcst(
        request: GetUltraSrtNcstRequest,
    ): ResponseEntity<GetUltraSrtNcstResponse> {
        val response = etcApiRestClient.getUltraSrtNcst(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            baseDate = request.baseDate,
            baseTime = request.baseTime,
            ny = request.ny,
            nx = request.nx,
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 기상청_단기예보 ((구) 동네예보) 조회서비스: 예보버전조회
     *
     * @param request [GetFcstVersionRequest]
     * @return [ResponseEntity]<[GetFcstVersionResponse]>
     *
     * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
     */
    @GetMapping("/getFcstVersion")
    fun getFcstVersion(
        request: GetFcstVersionRequest,
    ): ResponseEntity<GetFcstVersionResponse> {
        val response = etcApiRestClient.getFcstVersion(
            pageNo = request.pageNo,
            numOfRows = request.numOfRows,
            fileType = request.fileType,
            basedDatetime = request.basedDatetime,
        )

        return ResponseEntity.ok(response)
    }
}
