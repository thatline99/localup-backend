package thatline.localup.tourapi.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import thatline.localup.common.constant.CacheKeyGeneratorName
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRanking
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.VisitorStatistic
import thatline.localup.tourapi.restclient.TourApiRestClient
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.roundToInt

@Service
class TouristAttractionService(
    private val tourApiRestClient: TourApiRestClient,
) {
    //
    @Cacheable(
        cacheNames = [CacheObjectName.LAST_MONTHLY_TOURIST_ATTRACTION_RANKING_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.LAST_MONTHLY_TOURIST_ATTRACTION_RANKING,
        sync = true
    )
    fun findLastMonthlyTouristAttractionRanking(
        areaCode: String,
        sigunguCode: String,
    ): LastMonthlyTouristAttractionRankingInformation {
        val yearMonth = YearMonth.now().minusMonths(1)

        val response = tourApiRestClient.areaBasedList2(
            pageNo = 1,
            numOfRows = 100,
            baseYm = yearMonth.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMM),
            areaCd = areaCode,
            signguCd = sigunguCode,
        )

        val items = response.response.body.items.item

        // TODO: 예외 처리 필요

        val lastMonthlyTouristAttractionRankingList = items
            .map { item ->
                LastMonthlyTouristAttractionRanking(
                    rank = item.hubRank.toInt(),
                    name = item.hubTatsNm,
                    latitude = item.mapY.toDouble(),
                    longitude = item.mapX.toDouble(),
                    category = item.hubCtgryLclsNm,
                    subCategory = item.hubCtgryMclsNm,
                )
            }
            .sortedBy { it.rank }

        return LastMonthlyTouristAttractionRankingInformation(
            updatedDate = yearMonth.atDay(1).atStartOfDay(),
            lastMonthlyTouristAttractionRankingList = lastMonthlyTouristAttractionRankingList
        )
    }

    // TODO-noah: API의 한계로 별도의 배치 작업으로 개선하면 좋을 것 같음
    fun findLastYearSameWeekVisitorStatistics(
        sigunguCode: String,
    ): List<VisitorStatistic> {
        val (startDate, endDate) = DateTimeUtil.getLastYearSameIsoWeekRange()

        val startYmd = startDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val endYmd = endDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

        val response1 = tourApiRestClient.locgoRegnVisitrDDList(
            pageNo = 1,
            numOfRows = 1,
            startYmd = startYmd,
            endYmd = endYmd,
        )

        // TODO: 예외 처리 필요

        val response2 = tourApiRestClient.locgoRegnVisitrDDList(
            pageNo = 1,
            numOfRows = response1.response.body.totalCount,
            startYmd = startYmd,
            endYmd = endYmd,
        )

        // TODO: 예외 처리 필요

        val items = response2.response.body.items.item

        val visitorStatistics = items
            .filter { it.signguCode == sigunguCode }
            .groupBy { it.baseYmd }
            .map { (date, data) ->
                // 관광객 구분 코드
                val visitorCode = data.associateBy { it.touDivCd }

                VisitorStatistic(
                    date = LocalDate.parse(date, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd),
                    localVisitors = visitorCode["1"]?.touNum?.toDouble()?.roundToInt() ?: 0,
                    domesticVisitors = visitorCode["2"]?.touNum?.toDouble()?.roundToInt() ?: 0,
                    foreignVisitors = visitorCode["3"]?.touNum?.toDouble()?.roundToInt() ?: 0,
                )
            }
            .sortedBy { it.date }

        return visitorStatistics
    }
}
