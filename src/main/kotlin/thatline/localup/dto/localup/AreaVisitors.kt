package thatline.localup.dto.localup

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
 *
 * link: https://www.data.go.kr/data/15101972/openapi.do
 *
 * @author [noah (조태현)]
 */
data class AreaVisitors(
    val areaCode: String,
    val areaName: String,
    val visitorsByDate: List<DailyVisitors>,
)

data class DailyVisitors(
    val date: String,
    val visitors: List<VisitorByType>,
)

data class VisitorByType(
    val visitorTypeCode: String,
    val visitorCount: Double,
)
