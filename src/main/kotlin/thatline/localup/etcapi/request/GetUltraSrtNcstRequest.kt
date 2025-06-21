package thatline.localup.etcapi.request

/**
 * 기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회 요청
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property baseDate 발표 일자
 * @property baseTime 발표 시각
 * @property nx 예보 지점의 X 좌표
 * @property ny 예보 지점의 Y 좌표
 *
 * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
 */
data class GetUltraSrtNcstRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val baseDate: String,
    val baseTime: String,
    val nx: Int,
    val ny: Int,
)
