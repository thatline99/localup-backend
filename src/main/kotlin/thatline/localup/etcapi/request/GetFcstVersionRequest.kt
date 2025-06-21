package thatline.localup.etcapi.request

/**
 * 기상청_단기예보 ((구) 동네예보) 조회서비스: 예보버전조회 요청
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property fileType 파일 구분
 * @property basedDatetime 발표일시분
 */
data class GetFcstVersionRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val fileType: String,
    val basedDatetime: String,
)
