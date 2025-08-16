package thatline.localup.tourapi.service

import org.springframework.stereotype.Service
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRanking
import thatline.localup.tourapi.restclient.TourApiRestClient
import java.time.YearMonth

@Service
class TouristAttractionService(
    private val tourApiRestClient: TourApiRestClient,
) {
    fun findLastMonthlyTouristAttractionRanking(
        areaCode: String,
        sigunguCode: String,
    ): List<LastMonthlyTouristAttractionRanking> {
        val response = tourApiRestClient.areaBasedList2(
            pageNo = 1,
            numOfRows = 100,
            baseYm = YearMonth.now().minusMonths(1).format(DateTimeUtil.DATETIME_FORMATTER_yyyyMM),
            areaCd = areaCode,
            signguCd = sigunguCode,
        )

        val items = response.response.body.items.item

        // TODO: 예외 처리 필요

        return items
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
    }
}
