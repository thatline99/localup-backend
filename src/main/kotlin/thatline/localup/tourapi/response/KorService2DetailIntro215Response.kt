package thatline.localup.tourapi.response

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * 한국관광공사_국문 관광정보 서비스_GW: 소개 정보 조회
 *
 * @property response 응답
 *
 * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
 */
data class KorService2DetailIntro215Response(
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
    @JsonDeserialize(using = KorService2DetailIntro215ResponseItemsJsonDeserializer::class)
    data class Items(
        val item: List<Item>,
    )

    /**
     * 항목
     *
     * @property contentid 콘텐츠 ID
     * @property contenttypeid 콘텐츠 타입 ID
     * @property sponsor1 주최자 정보
     * @property sponsor1tel 주최자 연락처
     * @property sponsor2 주관사 정보
     * @property sponsor2tel 주관사 연락처
     * @property eventenddate 행사 종료일
     * @property playtime 공연 시간
     * @property eventplace 행사 장소
     * @property eventhomepage 행사 홈페이지
     * @property agelimit 관람 가능 연령
     * @property bookingplace 예매처
     * @property placeinfo 행사장 위치 안내
     * @property subevent 부대 행사
     * @property program 행사 프로그램
     * @property eventstartdate 행사 시작일
     * @property usetimefestival 이용 요금
     * @property discountinfofestival 할인 정보
     * @property spendtimefestival 관람 소요 시간
     * @property festivalgrade 축제 등급
     * @property progresstype 진행 상태 정보
     * @property festivaltype 축제 유형명
     */
    data class Item(
        val contentid: String,
        val contenttypeid: String,
        val sponsor1: String,
        val sponsor1tel: String,
        val sponsor2: String,
        val sponsor2tel: String,
        val eventenddate: String,
        val playtime: String,
        val eventplace: String,
        val eventhomepage: String,
        val agelimit: String,
        val bookingplace: String,
        val placeinfo: String,
        val subevent: String,
        val program: String,
        val eventstartdate: String,
        val usetimefestival: String,
        val discountinfofestival: String,
        val spendtimefestival: String,
        val festivalgrade: String,
        val progresstype: String,
        val festivaltype: String,
    )
}

private class KorService2DetailIntro215ResponseItemsJsonDeserializer :
    JsonDeserializer<KorService2DetailIntro215Response.Items>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): KorService2DetailIntro215Response.Items {
        val jsonNode = p.codec.readTree<JsonNode>(p)

        return when {
            // 빈 문자열인 경우
            jsonNode.isTextual && jsonNode.asText().isEmpty() -> {
                KorService2DetailIntro215Response.Items(emptyList())
            }

            // 정상
            jsonNode.isObject && jsonNode.has("item") -> {
                val itemJsonNode = jsonNode.get("item")

                val items = if (itemJsonNode.isArray) {
                    p.codec.treeToValue(itemJsonNode, Array<KorService2DetailIntro215Response.Item>::class.java)
                        .toList()
                } else {
                    emptyList()
                }
                KorService2DetailIntro215Response.Items(items)
            }

            else -> {
                KorService2DetailIntro215Response.Items(emptyList())
            }
        }
    }
}
