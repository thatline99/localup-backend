package thatline.localup.etcapi.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import thatline.localup.common.constant.CacheKeyGeneratorName
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.constant.TourApi
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.etcapi.dto.*
import thatline.localup.etcapi.response.GetVilageFcstResponse
import thatline.localup.etcapi.restclient.EtcApiRestClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class ForecastService(
    private val etcApiRestClient: EtcApiRestClient,
) {
    @Cacheable(
        cacheNames = [CacheObjectName.SHORT_TERM_FORECAST_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.SHORT_TERM_FORECAST_INFORMATION,
        sync = true
    )
    fun findShortTermForecast(
        sigunguCode: String,
        dateTime: LocalDateTime = LocalDateTime.now(),
    ): ShortTermForecastInformation {
        val (baseDate, baseTime) = getValidBaseDateAndTime(dateTime)

        // TODO: 로직 개선 필요
        val tourApiLocation = TourApi.getTourApiAreaBySigunguCode(sigunguCode)
            ?: throw IllegalArgumentException()

        // numOfRows: 기준 날짜(254개) + 내일(290개) + 모레(290개)
        // 1000개 사용 시 오늘, 내일, 모레 값 받을 수 있음
        val response = etcApiRestClient.getVilageFcst(
            pageNo = 1,
            numOfRows = 1000,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = tourApiLocation.nx,
            ny = tourApiLocation.ny,
        )

        // RestClient 단에서 검사하기 때문에 !! 사용
        val shortTermForecasts = convertToShortTermForecasts(response.response.body!!.items.item).take(3)

        return ShortTermForecastInformation(
            updatedDate = dateTime,
            shortTermForecasts = shortTermForecasts,
        )
    }

    fun convertToShortTermForecasts(items: List<GetVilageFcstResponse.Item>): List<ShortTermForecast> {
        // 날짜별로 그룹핑
        return items.groupBy { it.fcstDate }
            .map { (fcstDate, group) ->
                toShortTermForecast(fcstDate, group)
            }
            .sortedBy { it.date }
    }

    private fun toShortTermForecast(forecastDate: String, items: List<GetVilageFcstResponse.Item>): ShortTermForecast {
        val date = LocalDate.parse(forecastDate, DateTimeFormatter.BASIC_ISO_DATE)

        val byCategory = items.groupBy { it.category }
        val dailyMinimumTemperature: Double? = byCategory["TMN"]
            ?.mapNotNull { it.fcstValue.toDoubleOrNull() }
            ?.minOrNull()
        val dailyMaximumTemperature: Double? = byCategory["TMX"]
            ?.mapNotNull { it.fcstValue.toDoubleOrNull() }
            ?.maxOrNull()

        // 예보 시간별로 그룹핑
        val hourlyForecasts = items.groupBy { it.fcstTime }
            .map { (time, group) ->
                val map = group.associateBy { it.category }

                val pcp = map["PCP"]?.fcstValue
                val wsd = map["WSD"]?.fcstValue?.toDoubleOrNull()

                HourlyShortTermForecast(
                    time = LocalTime.parse(time, DateTimeUtil.DATETIME_FORMATTER_HHmm),

                    precipitationProbability = map["POP"]?.fcstValue?.toIntOrNull(),
                    precipitationType = map["PTY"]?.fcstValue?.let {
                        HourlyShortTermForecastPrecipitationType.fromCodeOrNull(
                            it
                        )
                    },
                    precipitationAmount = pcp?.let { HourlyShortTermForecastPrecipitationAmountType.fromCodeOrNull(it) },
                    humidity = map["REH"]?.fcstValue?.toIntOrNull(),
                    snowfallAmount = map["SNO"]?.fcstValue?.let {
                        HourlyShortTermForecastSnowfallAmountType.fromCodeOrNull(
                            it
                        )
                    },
                    skyCondition = map["SKY"]?.fcstValue?.let {
                        HourlyShortTermForecastSkyConditionType.fromCodeOrNull(
                            it
                        )
                    },
                    temperature = map["TMP"]?.fcstValue?.toDoubleOrNull(),
                    windUComponent = map["UUU"]?.fcstValue?.toDoubleOrNull(),
                    windVComponent = map["VVV"]?.fcstValue?.toDoubleOrNull(),
                    waveHeight = map["WAV"]?.fcstValue?.toIntOrNull(),
                    windDirection = map["VEC"]?.fcstValue?.toIntOrNull(),
                    windSpeed = wsd,
                    windSpeedType = wsd?.let { HourlyShortTermForecastWindSpeedType.fromValue(it) }
                )
            }
            .sortedBy { it.time }

        return ShortTermForecast(
            date = date,
            dailyMinimumTemperature = dailyMinimumTemperature,
            dailyMaximumTemperature = dailyMaximumTemperature,
            hourlyShortTermForecasts = hourlyForecasts,
        )
    }

    /**
     * 현재 시간을 기준으로 유효한 기상청 API baseDate와 baseTime을 반환합니다.
     * 기상청 단기예보는 하루 8번 (02, 05, 08, 11, 14, 17, 20, 23시) 발표됩니다.
     */
    private fun getValidBaseDateAndTime(dateTime: LocalDateTime): Pair<String, String> {
        val validTimes = listOf("0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300")
        val currentHour = dateTime.hour
        val currentMinute = dateTime.minute

        // 현재 시간을 4자리 문자열로 변환 (예: 13:30 -> "1330")
        val currentTime = String.format("%02d%02d", currentHour, currentMinute)

        // 현재 시간보다 이전인 가장 최근의 발표 시간 찾기
        val validBaseTime = validTimes
            .reversed() // 최신부터 확인
            .find { it <= currentTime }
            ?: validTimes.last() // 만약 02시 이전이라면 전날 23시 사용

        val baseDateTime = if (validBaseTime == validTimes.last() && currentTime < validTimes.first()) {
            // 02시 이전이면 전날 23시 데이터 사용
            dateTime.minusDays(1)
        } else {
            dateTime
        }

        val baseDate = baseDateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        return Pair(baseDate, validBaseTime)
    }
}
