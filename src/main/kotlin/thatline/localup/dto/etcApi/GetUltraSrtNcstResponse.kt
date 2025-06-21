package thatline.localup.dto.etcApi

/**
 * 기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회 응답
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
 */
data class GetUltraSrtNcstResponse(
    val response: Response,
) {
    /**
     * 응답
     * @property header 헤더
     * @property body 본문
     */
    data class Response(
        val header: Header,
        val body: Body?,
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
     * @property dataType 데이터 타입
     * @property items 목록
     * @property pageNo 페이지 번호
     * @property numOfRows 한 페이지 결과 수
     * @property totalCount 전체 결과 수
     */
    data class Body(
        val dataType: String,
        val items: Items,
        val pageNo: Int,
        val numOfRows: Int,
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
     * @property baseDate 발표 일자
     * @property baseTime 발표 시각
     * @property category 자료 구분 코드
     * @property nx 예보 지점 X 좌표
     * @property ny 예보 지점 Y 좌표
     * @property obsrValue 실황 값
     */
    data class Item(
        val baseDate: String,
        val baseTime: String,
        val category: String,
        val nx: Int,
        val ny: Int,
        val obsrValue: String,
    )
}
