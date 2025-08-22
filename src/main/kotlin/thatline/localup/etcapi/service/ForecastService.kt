package thatline.localup.etcapi.service

import org.springframework.stereotype.Service
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
    fun findShortTermForecast(
        sigunguCode: String,
        dateTime: LocalDateTime = LocalDateTime.now(),
    ): List<ShortTermForecast> {
        val baseDate = dateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val baseTime = "0200"

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

        return shortTermForecasts
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
}
