package thatline.localup.tourapi.request

/**
 * 한국관광공사_국문 관광정보 서비스_GW: 법정동코드조회 요청
 *
 * @property pageNo 페이지 번호
 * @property numOfRows 한 페이지 결과 수
 * @property lDongRegnCd 법정동 시군구 코드 (선택)
 *
 * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
 */
data class LdongCode2Request(
    val pageNo: Long,
    val numOfRows: Long,
    val lDongRegnCd: String?,
)
