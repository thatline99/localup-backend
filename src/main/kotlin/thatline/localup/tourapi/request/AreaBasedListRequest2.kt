package thatline.localup.tourapi.request

/**
 * 한국관광공사_기초지자체 중심 관광지 정보: 지역기반 중심 관광지 정보 목록 조회
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property baseYm 기준 날짜 조회
 * @property areaCd 중심지 지역 코드
 * @property signguCd 중심지 시군구 코드
 *
 * @see <a href="https://www.data.go.kr/data/15128559/openapi.do">공공데이터포털 API 문서</a>
 */
data class AreaBasedListRequest2(
    val pageNo: Long,
    val numOfRows: Long,
    val baseYm: String,
    val areaCd: String,
    val signguCd: String,
)
