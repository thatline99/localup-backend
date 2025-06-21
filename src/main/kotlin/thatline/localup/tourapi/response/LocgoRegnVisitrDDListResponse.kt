package thatline.localup.tourapi.response

data class LocgoRegnVisitrDDListResponse(
    val response: LocgoRegnVisitrDDListResponseWrapper = LocgoRegnVisitrDDListResponseWrapper(),
)

data class LocgoRegnVisitrDDListResponseWrapper(
    val header: LocgoRegnVisitrDDListHeader = LocgoRegnVisitrDDListHeader(),
    val body: LocgoRegnVisitrDDListBody = LocgoRegnVisitrDDListBody(),
)

data class LocgoRegnVisitrDDListHeader(
    val resultCode: String = "",
    val resultMsg: String = "",
)

data class LocgoRegnVisitrDDListBody(
    val items: LocgoRegnVisitrDDListItems = LocgoRegnVisitrDDListItems(),
    val numOfRows: Long = 0,
    val pageNo: Long = 0,
    val totalCount: Long = 0,
)

data class LocgoRegnVisitrDDListItems(
    val item: List<LocgoRegnVisitrDDListItem> = emptyList(),
)

data class LocgoRegnVisitrDDListItem(
    val signguCode: String = "",
    val signguNm: String = "",
    val daywkDivCd: String = "",
    val daywkDivNm: String = "",
    val touDivCd: String = "",
    val touDivNm: String = "",
    val touNum: String = "",
    val baseYmd: String = "",
)
