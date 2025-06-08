package thatline.localup.request

data class MetcoRegnVisitrDDListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val startYmd: String,
    val endYmd: String,
)
