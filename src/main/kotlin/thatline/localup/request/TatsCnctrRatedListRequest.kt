package thatline.localup.request

data class TatsCnctrRatedListRequest(
    val pageNo: Long,
    val numOfRows: Long,
    val areaCd: String,
    val signguCd: String,
    val tAtsNm: String = "",
)
