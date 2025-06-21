package thatline.localup.tourapi.response

data class MetcoRegnVisitrDDListResponse(
    val response: MetcoRegnVisitrDDListResponseWrapper = MetcoRegnVisitrDDListResponseWrapper(),
)

data class MetcoRegnVisitrDDListResponseWrapper(
    val header: MetcoRegnVisitrDDListHeader = MetcoRegnVisitrDDListHeader(),
    val body: MetcoRegnVisitrDDListBody = MetcoRegnVisitrDDListBody(),
)

data class MetcoRegnVisitrDDListHeader(
    val resultCode: String = "",
    val resultMsg: String = "",
)

data class MetcoRegnVisitrDDListBody(
    val items: MetcoRegnVisitrDDListItems = MetcoRegnVisitrDDListItems(),
    val numOfRows: Long = 0,
    val pageNo: Long = 0,
    val totalCount: Long = 0,
)

data class MetcoRegnVisitrDDListItems(
    val item: List<MetcoRegnVisitrDDListItem> = emptyList(),
)

data class MetcoRegnVisitrDDListItem(
    val areaCode: String = "",
    val areaNm: String = "",
    val daywkDivCd: String = "",
    val daywkDivNm: String = "",
    val touDivCd: String = "",
    val touDivNm: String = "",
    val touNum: String = "",
    val baseYmd: String = "",
)
