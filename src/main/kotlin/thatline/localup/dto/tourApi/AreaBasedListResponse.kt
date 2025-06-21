package thatline.localup.dto.tourApi

/**
 * 한국관광공사_관광지별 연관 관광지 정보: 지역기반 관광지별 연관 관광지 정보 목록 조회 응답
 *
 * @param response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15128560/openapi.do">공공데이터포털 API 문서</a>
 */
data class AreaBasedListResponse(
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
        val numOfRows: Int,
        val pageNo: Int,
        val totalCount: Int,
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
     * @property baseYm 기준연월
     * @property tAtsCd 관광지 코드
     * @property tAtsNm 관광지명
     * @property areaCd 관광지 지역 코드
     * @property areaNm 관광지 지역명
     * @property signguCd 관광지 시군구 코드
     * @property signguNm 관광지 시군구명
     * @property rlteTatsCd 연관 관광지 코드
     * @property rlteTatsNm 연관 관광지명
     * @property rlteRegnCd 연관 관광지 지역코드
     * @property rlteRegnNm 연관 관광지 지역명
     * @property rlteSignguCd 연관 관광지 시군구 코드
     * @property rlteSignguNm 연관 관광지 시군구명
     * @property rlteCtgryLclsNm 연관 관광지 대분류명
     * @property rlteCtgryMclsNm 연관 관광지 중분류명
     * @property rlteCtgrySclsNm 연관 관광지 소분류명
     * @property rlteRank 연관 순위
     */
    data class Item(
        val baseYm: String,
        val tAtsCd: String,
        val tAtsNm: String,
        val areaCd: String,
        val areaNm: String,
        val signguCd: String,
        val signguNm: String,
        val rlteTatsCd: String,
        val rlteTatsNm: String,
        val rlteRegnCd: String,
        val rlteRegnNm: String,
        val rlteSignguCd: String,
        val rlteSignguNm: String,
        val rlteCtgryLclsNm: String,
        val rlteCtgryMclsNm: String,
        val rlteCtgrySclsNm: String,
        val rlteRank: String,
    )
}
