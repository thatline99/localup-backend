package thatline.localup.tourapi.response

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * 한국관광공사_국문 관광정보 서비스_GW: 지역 기반 관광 정보 조회
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
 */
data class KorService2AreaBasedList2Response(
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
    @JsonDeserialize(using = ItemsJsonDeserializer::class)
    data class Items(
        val item: List<Item>,
    )

    /**
     * 항목
     *
     * @property addr1 주소
     * @property addr2 상세 주소
     * @property areacode 지역 코드
     * @property cat1 대분류
     * @property cat2 중분류
     * @property cat3 소분류
     * @property contentid 콘텐츠 ID
     * @property contenttypeid 콘텐츠 타입 ID
     * @property createdtime 등록일
     * @property firstimage 대표 이미지 (원본)
     * @property firstimage2 대표 이미지 (썸네일)
     * @property cpyrhtDivCd 저작권 유형 (Type1: 제 1 유형 (출처 표시 - 권장), Type3 : 제 3 유형 (제 1 유형 + 변경 금지)
     * @property mapx GPS X좌표 (경도)
     * @property mapy GPS Y좌표 (위도)
     * @property mlevel Map Level
     * @property modifiedtime 수정일
     * @property sigungucode 시군구 코드
     * @property tel 전화 번호
     * @property title 제목
     * @property zipcode 우변 번호
     * @property lDongRegnCd 법정동 시도 코드
     * @property lDongSignguCd 법정동 시군구 코드
     * @property lclsSystm1 분류 체계 대분류
     * @property lclsSystm2 분류 체계 중분류
     * @property lclsSystm3 분류 체계 소분류
     */
    data class Item(
        val addr1: String,
        val addr2: String,
        val areacode: String,
        val cat1: String,
        val cat2: String,
        val cat3: String,
        val contentid: String,
        val contenttypeid: String,
        val createdtime: String,
        val firstimage: String,
        val firstimage2: String,
        val cpyrhtDivCd: String,
        val mapx: String,
        val mapy: String,
        val mlevel: String,
        val modifiedtime: String,
        val sigungucode: String,
        val tel: String,
        val title: String,
        val zipcode: String,
        val lDongRegnCd: String,
        val lDongSignguCd: String,
        val lclsSystm1: String,
        val lclsSystm2: String,
        val lclsSystm3: String,
    )
}

private class ItemsJsonDeserializer : JsonDeserializer<KorService2AreaBasedList2Response.Items>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): KorService2AreaBasedList2Response.Items {
        val jsonNode = p.codec.readTree<JsonNode>(p)

        return when {
            // 빈 문자열인 경우
            jsonNode.isTextual && jsonNode.asText().isEmpty() -> {
                KorService2AreaBasedList2Response.Items(emptyList())
            }

            // 정상
            jsonNode.isObject && jsonNode.has("item") -> {
                val itemJsonNode = jsonNode.get("item")

                val items = if (itemJsonNode.isArray) {
                    p.codec.treeToValue(itemJsonNode, Array<KorService2AreaBasedList2Response.Item>::class.java)
                        .toList()
                } else {
                    emptyList()
                }
                KorService2AreaBasedList2Response.Items(items)
            }

            else -> {
                KorService2AreaBasedList2Response.Items(emptyList())
            }
        }
    }
}
