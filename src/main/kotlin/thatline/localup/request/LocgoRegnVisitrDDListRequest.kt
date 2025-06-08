package thatline.localup.request

data class LocgoRegnVisitrDDListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val startYmd: String,
    val endYmd: String,
)
