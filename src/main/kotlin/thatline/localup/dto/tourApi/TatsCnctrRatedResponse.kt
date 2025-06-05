package thatline.localup.dto.tourApi

// TODO: noah, 수정

data class TatsCnctrRatedResponse(
    val response: Response,
)

data class Response(
    val header: Header,
    val body: Body,
)

data class Header(
    val resultCode: String,
    val resultMsg: String,
)

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int,
)

data class Items(
    val item: List<Item>,
)

//data class Item(
//    val areaCd: Int,
//    val areaNm: String,
//    val signguCd: Int,
//    val signguNm: String,
//    val tAtsCd: String,
//    val tAtsNm: String,
//    val baseYmd: String,
//    val date: String,
//    val cncntrRate: Double
//)

data class Item(
    val areaCd: Int?,
    val areaNm: String?,
    val signguCd: Int?,
    val signguNm: String?,
    val tAtsCd: String?,
    val tAtsNm: String?,
    val baseYmd: String?,
    val date: String?,
    val cncntrRate: Double?,
)
