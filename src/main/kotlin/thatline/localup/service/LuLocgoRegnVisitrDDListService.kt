package thatline.localup.service

import org.springframework.stereotype.Service
import thatline.localup.dto.localup.DailySignguStatistics
import thatline.localup.dto.localup.SignguStatistics
import thatline.localup.dto.localup.SignguVisitor
import thatline.localup.util.DateUtil.DATETIME_FORMATTER
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회
 *
 * link: https://www.data.go.kr/data/15101972/openapi.do
 *
 * @author [noah (조태현)]
 */
@Service
class LuLocgoRegnVisitrDDListService(
    private val tourApiService: TourApiService,
) {
    fun searchData(
        startDate: LocalDate,
        endDate: LocalDate,
        signguCode: String,
    ): SignguStatistics? {
        val formattedStartDate = startDate.format(DATETIME_FORMATTER)
        val formattedEndDate = endDate.format(DATETIME_FORMATTER)

        val totalCount = tourApiService.locgoRegnVisitrDDList(
            1,
            1,
            formattedStartDate,
            formattedEndDate
        ).response.body.totalCount

        val items = tourApiService.locgoRegnVisitrDDList(
            1,
            totalCount,
            formattedStartDate,
            formattedEndDate
        ).response.body.items.item

        val filteredItems = items.filter { it.signguCode == signguCode }

        if (filteredItems.isEmpty()) {
            return null
        }

        val dailySignguStatistics = filteredItems
            .groupBy { it.baseYmd }
            .map { (date, items) ->
                val formattedDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE)
                    .format(DateTimeFormatter.ISO_DATE)

                val signguVisitors = items.map {
                    SignguVisitor(
                        typeCode = it.touDivCd,
                        count = it.touNum.toDouble(),
                    )
                }

                DailySignguStatistics(
                    date = formattedDate,
                    signguVisitors = signguVisitors
                )
            }
            .sortedBy { it.date }

        return SignguStatistics(
            signguCode = signguCode,
            signguName = filteredItems.first().signguNm,
            dailySignguStatistics = dailySignguStatistics
        )
    }
}
