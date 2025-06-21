package thatline.localup.tourapi.response

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15101972/openapi.do">공공데이터포털 API 문서</a>
 */
data class MetcoRegnVisitrDDListResponse(
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
     * @property areaCode 시도 코드
     * @property areaNm 시도명
     * @property daywkDivCd 요일 구분 코드
     * @property daywkDivNm 요일 구분명
     * @property touDivCd 관광객 구분 코드
     * @property touDivNm 관광객 구분명
     * @property touNum 관광객 수
     * @property baseYmd 기준 연월일
     */
    data class Item(
        val areaCode: String,
        val areaNm: String,
        val daywkDivCd: String,
        val daywkDivNm: String,
        val touDivCd: String,
        val touDivNm: String,
        val touNum: String,
        val baseYmd: String,
    )
}
