package thatline.localup.dto.localup

data class MetcoAreaVisitors(
    val areaCode: String,
    val areaName: String,
    val visitorsByDate: List<MetcoVisitorsByDate>,
)

data class MetcoVisitorsByDate(
    val date: String,
    val visitors: List<MetcoVisitorItem>,
)

data class MetcoVisitorItem(
    val touDivCd: String,
    val touNum: Double,
)

