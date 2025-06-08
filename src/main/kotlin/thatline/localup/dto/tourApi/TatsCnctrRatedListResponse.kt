package thatline.localup.dto.tourApi

data class TatsCnctrRatedListResponse(
    val response: TatsCnctrRatedListResponseWrapper = TatsCnctrRatedListResponseWrapper(),
)

data class TatsCnctrRatedListResponseWrapper(
    val header: TatsCnctrRatedListHeader = TatsCnctrRatedListHeader(),
    val body: TatsCnctrRatedListBody = TatsCnctrRatedListBody(),
)

data class TatsCnctrRatedListHeader(
    val resultCode: String = "",
    val resultMsg: String = "",
)

data class TatsCnctrRatedListBody(
    val items: TatsCnctrRatedListItems = TatsCnctrRatedListItems(),
    val numOfRows: Long = 0,
    val pageNo: Long = 0,
    val totalCount: Long = 0,
)

data class TatsCnctrRatedListItems(
    val item: List<TatsCnctrRatedListItem> = emptyList(),
)

data class TatsCnctrRatedListItem(
    val baseYmd: String = "",
    val areaCd: String = "",
    val areaNm: String = "",
    val signguCd: String = "",
    val signguNm: String = "",
    val tAtsNm: String = "",
    val cnctrRate: Double = 0.0,
)
