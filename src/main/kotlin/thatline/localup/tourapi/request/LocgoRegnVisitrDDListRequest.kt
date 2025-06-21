package thatline.localup.tourapi.request

data class LocgoRegnVisitrDDListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val startYmd: String,
    val endYmd: String,
)
