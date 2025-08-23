package thatline.localup.tourapi.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import thatline.localup.common.constant.CacheKeyGeneratorName
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.common.util.GeoUtil
import thatline.localup.tourapi.dto.*
import thatline.localup.tourapi.response.KorService2SearchFestival2Response
import thatline.localup.tourapi.restclient.TourApiRestClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Service
class TouristAttractionService(
    private val tourApiRestClient: TourApiRestClient,
) {
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

        val response = tourApiRestClient.locgoHubTarService1AreaBasedList2(
            pageNo = 1,
            numOfRows = 100,
            baseYm = yearMonth.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMM),
            areaCd = areaCode,
            signguCd = sigunguCode,
        )

        val items = response.response.body.items.item

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
    @Cacheable(
        cacheNames = [CacheObjectName.LAST_YEAR_SAME_WEEK_VISITOR_STATISTICS_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.LAST_YEAR_SAME_WEEK_VISITOR_STATISTICS,
        sync = true
    )
    fun findLastYearSameWeekVisitorStatistics(
        sigunguCode: String,
    ): LastYearSameWeekVisitorStatisticsInformation {
        val (startDate, endDate) = DateTimeUtil.getLastYearSameIsoWeekRange()

        val startYmd = startDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val endYmd = endDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

        val response1 = tourApiRestClient.dataLabServiceLocgoRegnVisitrDDList(
            pageNo = 1,
            numOfRows = 1,
            startYmd = startYmd,
            endYmd = endYmd,
        )

        val response2 = tourApiRestClient.dataLabServiceLocgoRegnVisitrDDList(
            pageNo = 1,
            numOfRows = response1.response.body.totalCount,
            startYmd = startYmd,
            endYmd = endYmd,
        )

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

        return LastYearSameWeekVisitorStatisticsInformation(
            updatedDate = LocalDateTime.now(),
            visitorStatistics = visitorStatistics
        )
    }

    @Cacheable(
        cacheNames = [CacheObjectName.SIGUNGU_MAIN_EVENT_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.SIGUNGU_MAIN_EVENT,
        sync = true
    )
    fun findSigunguMainEvent(
        sigunguCode: String,
        latitude: Double,
        longitude: Double,
    ): SigunguMainEventInformation {
        val now = LocalDate.now()

        val events = findSigunguEvents(
            sigunguCode = sigunguCode,
            startDate = now,
            endDate = now.withDayOfMonth(now.lengthOfMonth()),
        )

        // TODO: 로직 수정
        val filteredEvent =
            // 1. 오늘 시작
            events.filter {
                runCatching { LocalDate.parse(it.eventstartdate, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd) }
                    .getOrNull() == now
            }.minWithOrNull(
                compareBy(
                    {
                        val eventLatitude = it.mapy.toDoubleOrNull() ?: Double.MAX_VALUE
                        val eventLongitude = it.mapx.toDoubleOrNull() ?: Double.MAX_VALUE
                        GeoUtil.distanceInKilometer(latitude, longitude, eventLatitude, eventLongitude)
                    },
                    { it.title }
                )
            )
            // 2. 곧 시작
                ?: events.filter {
                    val startDate =
                        runCatching { LocalDate.parse(it.eventstartdate, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd) }
                            .getOrNull()
                    startDate != null && startDate.isAfter(now)
                }.minWithOrNull(
                    compareBy(
                        {
                            val eventLatitude = it.mapy.toDoubleOrNull() ?: Double.MAX_VALUE
                            val eventLongitude = it.mapx.toDoubleOrNull() ?: Double.MAX_VALUE
                            GeoUtil.distanceInKilometer(latitude, longitude, eventLatitude, eventLongitude)
                        },
                        { it.title }
                    )
                )
                // 3. 진행 중
                ?: events.filter {
                    val startDate =
                        runCatching { LocalDate.parse(it.eventstartdate, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd) }
                            .getOrNull()
                    val endDate =
                        runCatching { LocalDate.parse(it.eventenddate, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd) }
                            .getOrNull()
                    startDate != null && endDate != null && startDate <= now && now <= endDate
                }.minWithOrNull(
                    compareBy(
                        {
                            val eventLatitude = it.mapy.toDoubleOrNull() ?: Double.MAX_VALUE
                            val eventLongitude = it.mapx.toDoubleOrNull() ?: Double.MAX_VALUE
                            GeoUtil.distanceInKilometer(latitude, longitude, eventLatitude, eventLongitude)
                        },
                        { it.title }
                    )
                )

        return SigunguMainEventInformation(
            updatedDate = LocalDateTime.now(),
            sigunguMainEvent = filteredEvent?.let {
                LocationEvent(
                    contentTypeId = it.contenttypeid,
                    contentId = it.contentid,
                    title = it.title,
                    startDate = LocalDate.parse(it.eventstartdate, DateTimeFormatter.BASIC_ISO_DATE),
                    endDate = LocalDate.parse(it.eventenddate, DateTimeFormatter.BASIC_ISO_DATE),
                    zipCode = it.zipcode,
                    address = listOf(it.addr1, it.addr2).filter { addr -> addr.isNotBlank() }.joinToString(", "),
                    latitude = it.mapy.toDouble(),
                    longitude = it.mapx.toDouble(),
                    telephone = it.tel,
                    originalImageUrl = it.firstimage,
                    thumbnailImageUrl = it.firstimage2,
                )
            }
        )
    }

    @Cacheable(
        cacheNames = [CacheObjectName.ONGOING_OR_UPCOMING_SIGUNGU_EVENTS_FROM_TODAY_TO_MONTH_END_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.ONGOING_OR_UPCOMING_SIGUNGU_EVENTS_FROM_TODAY_TO_MONTH_END,
        sync = true
    )
    fun findOngoingOrUpComingSigunguEventsFromTodayToMonthEnd(
        sigunguCode: String,
    ): OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation {
        val now = LocalDate.now()

        val events = findSigunguEvents(
            sigunguCode = sigunguCode,
            startDate = now,
            endDate = now.withDayOfMonth(now.lengthOfMonth()),
        )

        val sigunguEventsWithDates = events
            .map { it ->
                with(it) {
                    LocationEvent(
                        contentTypeId = contenttypeid,
                        contentId = contentid,
                        title = title,
                        startDate = LocalDate.parse(eventstartdate, DateTimeFormatter.BASIC_ISO_DATE),
                        endDate = LocalDate.parse(eventenddate, DateTimeFormatter.BASIC_ISO_DATE),
                        zipCode = zipcode,
                        address = listOf(addr1, addr2).filter { it.isNotBlank() }.joinToString(", "),
                        latitude = mapy.toDouble(),
                        longitude = mapx.toDouble(),
                        telephone = tel,
                        originalImageUrl = firstimage,
                        thumbnailImageUrl = firstimage2,
                    )
                }
            }

        // 오늘 날짜(행사 종료일, 제목, 컨텐츠 ID), 진행 중(행사 종료일, 행사 시작일, 제목, 컨텐츠 ID), 진행 예정(행사 시작일, 행사 종료일, 제목, 컨텐츠 ID) 정렬로 진행

        // 1) 오늘 시작 / 나머지
        val (todayStartEvents, restEvents) = sigunguEventsWithDates.partition { it.startDate == now }
        // 2) 진행 중 / 진행 예정
        val (ongoingEvents, upcomingEvents) = restEvents.partition { it.startDate <= now && now <= it.endDate }

        // 3) 섹션별 정렬 후 합치기
        val sortedEvents = todayStartEvents.sortedWith(compareBy({ it.endDate }, { it.title }, { it.contentId })) +
                ongoingEvents.sortedWith(compareBy({ it.endDate }, { it.startDate }, { it.title }, { it.contentId })) +
                upcomingEvents.sortedWith(compareBy({ it.startDate }, { it.endDate }, { it.title }, { it.contentId }))

        return OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation(
            updatedDate = LocalDateTime.now(),
            sigunguEvents = sortedEvents
        )
    }

    private fun findSigunguEvents(
        sigunguCode: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<KorService2SearchFestival2Response.Item> {
        val startDateFormat = startDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val endDateFormat = endDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

        val response1 = tourApiRestClient.korService2SearchFestival2(
            pageNo = 1,
            numOfRows = 1,
            eventStartDate = startDateFormat,
            eventEndDate = endDateFormat,
            lDongRegnCd = sigunguCode.substring(0, 2),
            lDongSignguCd = sigunguCode.substring(2, 5),
        )

        val response2 = tourApiRestClient.korService2SearchFestival2(
            pageNo = 1,
            numOfRows = response1.response.body.totalCount,
            eventStartDate = startDateFormat,
            eventEndDate = endDateFormat,
            lDongRegnCd = sigunguCode.substring(0, 2),
            lDongSignguCd = sigunguCode.substring(2, 5),
        )

        return response2.response.body.items.item
    }
}
