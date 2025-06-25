package thatline.localup.tourapi.response

/**
 * 한국관광공사_기초지자체 중심 관광지 정보: 지역기반 중심 관광지 정보 목록 조회
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15128559/openapi.do">공공데이터포털 API 문서</a>
 */
data class AreaBasedListResponse2(
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
     * @property baseYm 기준 연월
     * @property mapX X좌표 값
     * @property mapY Y좌표 값
     * @property areaCd 지역 코드
     * @property areaNm 지역명
     * @property signguCd 시군구 코드
     * @property signguNm 시군구명
     * @property hubTatsCd 중심지 관광지 코드
     * @property hubTatsNm 중심지 관광지명
     * @property hubCtgryLclsNm 중심지 카테고리 대분류명
     * @property hubCtgryMclsNm 중심지 카테고리 중분류명
     * @property hubRank 중심지 순위
     */
    data class Item(
        val baseYm: String,
        val mapX: String,
        val mapY: String,
        val areaCd: String,
        val areaNm: String,
        val signguCd: String,
        val signguNm: String,
        val hubTatsCd: String,
        val hubTatsNm: String,
        val hubCtgryLclsNm: String,
        val hubCtgryMclsNm: String,
        val hubRank: String,
    )
}
