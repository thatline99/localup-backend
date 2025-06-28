package thatline.localup.tourapi.response

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * 한국관광공사_국문 관광정보 서비스_GW: 지역코드조회 응답
 *
 * @property response 응답
 */
data class AreaCode2Response(
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
        val items: Any,
        val numOfRows: Long,
        val pageNo: Long,
        val totalCount: Long,
    ) {
        // 특정 areaCode에 데이터가 없을 경우, items 필드가 빈 문자열로 응답되는 케이스를 처리하기 위한 메서드
        fun getItems(mapper: ObjectMapper): List<Item> {
            val items = (items as? Map<*, *>)?.get("item") as? List<*>

            return items?.map { mapper.convertValue(it, Item::class.java) } ?: emptyList()
        }
    }

    /**
     * 목록
     *
     * @property item 항목
     */
    data class Items(
        val item: List<Item>,
    )

    /**
     * 항목
     *
     * @property rnum 일련번호
     * @property code 지역 코드 또는 시군구 코드
     * @property name 지역명 또는 시군구명
     */
    data class Item(
        val rnum: Long,
        val code: String,
        val name: String,
    )
}
