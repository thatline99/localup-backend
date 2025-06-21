package thatline.localup.localup.response.dto

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
 *
 * link: https://www.data.go.kr/data/15101972/openapi.do
 *
 * @author [noah (조태현)]
 */
data class SignguStatistics(
    val signguCode: String,
    val signguName: String,
    val dailySignguStatistics: List<DailySignguStatistics>,
)

data class DailySignguStatistics(
    val date: String,
    val signguVisitors: List<SignguVisitor>,
)

data class SignguVisitor(
    val typeCode: String,
    val count: Double,
)
