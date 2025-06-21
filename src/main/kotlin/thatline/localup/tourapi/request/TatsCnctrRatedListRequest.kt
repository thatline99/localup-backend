package thatline.localup.tourapi.request

/**
 * 한국관광공사_관광지 집중률 방문자 추이 예측 정보: 관광지 집중률 정보 목록조회 요청
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property areaCd 관광지 지역 코드
 * @property signguCd 관광지 시군구 코드
 * @property tAtsNm 관광지명
 *
 * @see <a href="https://www.data.go.kr/data/15128555/openapi.do">공공데이터포털 API 문서</a>
 */
data class TatsCnctrRatedListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val areaCd: String,
    val signguCd: String,
    val tAtsNm: String = "",
)
