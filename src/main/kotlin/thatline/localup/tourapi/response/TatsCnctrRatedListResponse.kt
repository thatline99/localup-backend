package thatline.localup.tourapi.response

/**
 * 한국관광공사_관광지 집중률 방문자 추이 예측 정보: 관광지 집중률 정보 목록조회
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15128555/openapi.do">공공데이터포털 API 문서</a>
 */
data class TatsCnctrRatedListResponse(
    val response: Response,
) {
    /**
     * 응답
     *
     * @property header 헤더
     * @property body 본문
     */
    data class Response(
        val header: Header,
        val body: Body,
    )

    /**
     * 헤더
     *
     * @property resultCode 결과 코드
     * @property resultMsg 결과 메시지
     */
    data class Header(
        val resultCode: String,
        val resultMsg: String,
    )

    /**
     * 본문
     *
     * @property items 목록
     * @property numOfRows 한 페이지 결과 수
     * @property pageNo 페이지 번호
     * @property totalCount 전체 결과 수
     */
    data class Body(
        val items: Items,
        val numOfRows: Long,
        val pageNo: Long,
        val totalCount: Long,
    )

    /**
     * 목록
     *
     * @property item 항목들
     */
    data class Items(
        val item: List<Item>,
    )

    /**
     * 항목
     *
     * @property baseYmd 기준 연월일
     * @property areaCd 지역 코드
     * @property areaNm 지역명
     * @property signguCd 시군구 코드
     * @property signguNm 시군구명
     * @property tAtsNm 관광지명
     * @property cnctrRate 집중률
     */
    data class Item(
        val baseYmd: String,
        val areaCd: String,
        val areaNm: String,
        val signguCd: String,
        val signguNm: String,
        val tAtsNm: String,
        val cnctrRate: Double,
    )
}
