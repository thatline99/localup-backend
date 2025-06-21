package thatline.localup.tourapi.request

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회 요청
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property startYmd 시작 연월일
 * @property endYmd 종료 연월일
 *
 * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
 */
data class MetcoRegnVisitrDDListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val startYmd: String,
    val endYmd: String,
)
