package thatline.localup.service

import org.springframework.stereotype.Service
import thatline.localup.dto.localup.AreaStatistics
import thatline.localup.dto.localup.AreaVisitor
import thatline.localup.dto.localup.DailyAreaStatistics
import thatline.localup.util.DateUtil.DATETIME_FORMATTER
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 한국관광공사_관광빅데이터 정보서비스_GW: 광역 지자체 지역방문자수 집계 데이터 정보 조회
 *
 * link: https://www.data.go.kr/data/15101972/openapi.do
 *
 * @author [noah (조태현)]
 */
@Service
class LuMetcoRegnVisitrDDListService(
    private val tourApiService: TourApiService,
) {
    fun searchData(
        startDate: LocalDate,
        endDate: LocalDate,
        areaCode: String,
    ): AreaStatistics? {
        val formattedStartDate = startDate.format(DATETIME_FORMATTER)
        val formattedEndDate = endDate.format(DATETIME_FORMATTER)

        val totalCount = tourApiService.metcoRegnVisitrDDList(
            1,
            1,
            formattedStartDate,
            formattedEndDate
        ).response.body.totalCount

        val items = tourApiService.metcoRegnVisitrDDList(
            1,
            totalCount,
            formattedStartDate,
            formattedEndDate
        ).response.body.items.item

        val filteredItems = items.filter { it.areaCode == areaCode }

        if (filteredItems.isEmpty()) {
            return null
        }

        val dailyAreaStatistics = filteredItems
            .groupBy { it.baseYmd }
            .map { (date, items) ->
                val formattedDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE)
                    .format(DateTimeFormatter.ISO_DATE)

                val areaVisitors = items.map {
                    AreaVisitor(
                        typeCode = it.touDivCd,
                        count = it.touNum.toDouble()
                    )
                }

                DailyAreaStatistics(
                    date = formattedDate,
                    areaVisitors = areaVisitors
                )
            }
            .sortedBy { it.date }

        return AreaStatistics(
            areaCode = areaCode,
            areaName = filteredItems.first().areaNm,
            dailyAreaStatistics = dailyAreaStatistics,
        )
    }
}
