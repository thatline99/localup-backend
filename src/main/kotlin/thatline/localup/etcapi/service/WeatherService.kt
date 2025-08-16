package thatline.localup.etcapi.service

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import thatline.localup.common.constant.CacheKeyGeneratorName
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.constant.TourApi
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.etcapi.code.UltraSrtNcstResponseCode
import thatline.localup.etcapi.dto.DailyWeather
import thatline.localup.etcapi.dto.WeatherCondition
import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.etcapi.restclient.EtcApiRestClient
import thatline.localup.localup.exception.WeatherServiceException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class WeatherService(
    private val etcApiRestClient: EtcApiRestClient,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Cacheable(
        cacheNames = [CacheObjectName.WEATHER_INFORMATION],
        keyGenerator = CacheKeyGeneratorName.WEATHER_INFORMATION,
        sync = true
    )
    fun getThreeDayWeatherSummaries(
        sigunguCode: String,
    ): WeatherInformation {
        val baseDateTime = LocalDateTime.now()
            .minusMinutes(10) // 단기예보조회, API 제공 시간 10분 보정
            .truncatedTo(ChronoUnit.HOURS)
        val baseDate = baseDateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val baseTime = "0200"

        // TODO: 로직 개선 필요
        val tourApiArea = TourApi.getTourApiAreaBySigunguCode(sigunguCode)
            ?: throw IllegalArgumentException()

        val response = etcApiRestClient.getVilageFcst(
            pageNo = 1,
            numOfRows = 834, // 기준 날짜(254개) + 내일(290개) + 모레(290개)
            baseDate = baseDate,
            baseTime = baseTime,
            nx = tourApiArea.nx,
            ny = tourApiArea.ny,
        )

        if (response.response.header.resultCode != "00") {
            log.warn(
                "resultCode={}, resultMsg={}",
                response.response.header.resultCode,
                response.response.header.resultMsg
            )

            // TODO: 로직 재사용함, 수정 필요
            throw WeatherServiceException(
                message = UltraSrtNcstResponseCode.from(response.response.header.resultCode).message
            )
        }

        val body = response.response.body
            ?: throw WeatherServiceException(message = "BODY_IS_NULL")

        val dailyWeatherList = body.items.item
            .groupBy { it.fcstDate }
            .toSortedMap().entries.map { (date, items) ->
                val precipitationTypeValues = items
                    .filter { it.category == "PTY" }
                    .map { it.fcstValue }

                val skyConditionValues = items
                    .filter { it.category == "SKY" }
                    .map { it.fcstValue }

                val condition =
                    if (precipitationTypeValues.any { it != "0" }) {
                        determinePrecipitationCondition(precipitationTypeValues)
                    } else {
                        determineSkyCondition(skyConditionValues)
                    }

                val minimumTemperature = items.first { it.category == "TMN" }.fcstValue.toDouble()
                val maximumTemperature = items.first { it.category == "TMX" }.fcstValue.toDouble()

                DailyWeather(
                    date = LocalDate.parse(date, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd),
                    condition = condition,
                    minimumTemperature = minimumTemperature,
                    maximumTemperature = maximumTemperature,
                )
            }

        return WeatherInformation(
            updatedDate = getThreeHourBaseDateTime(),
            dailyWeatherList = dailyWeatherList,
        )
    }

    private fun determinePrecipitationCondition(precipitationTypeValues: List<String>): WeatherCondition {
        val set = precipitationTypeValues.toSet()

        return when {
            set.any { it == "3" || it == "7" } -> WeatherCondition.SNOW
            set.any { it == "2" || it == "6" } -> WeatherCondition.RAIN_SNOW
            set.any { it == "1" || it == "5" } -> WeatherCondition.RAIN
            set.any { it == "4" } -> WeatherCondition.SHOWER
            else -> WeatherCondition.UNKNOWN
        }
    }

    private fun determineSkyCondition(skyConditionValues: List<String>): WeatherCondition {
        if (skyConditionValues.isEmpty()) {
            return WeatherCondition.UNKNOWN
        }

        // 코드별 빈도 집계
        val countsByCode: Map<String, Int> =
            skyConditionValues.groupingBy { it }.eachCount()

        // 최대 빈도 계산
        val maxFrequency: Int =
            countsByCode.values.maxOf { it }

        // 최빈값(동률 가능) 집합 추출
        val modes: Set<String> =
            countsByCode.filterValues { it == maxFrequency }.keys

        // 대표 코드 선택(우선순위: 4>3>1, 그 외 임의 1개)
        val resolvedCode: String = when {
            "4" in modes -> "4"   // 흐림
            "3" in modes -> "3"   // 구름많음
            "1" in modes -> "1"   // 맑음
            else -> modes.first()
        }

        return when (resolvedCode) {
            "4" -> WeatherCondition.CLOUDY
            "3" -> WeatherCondition.PARTLY_CLOUDY
            "1" -> WeatherCondition.SUNNY
            else -> WeatherCondition.UNKNOWN
        }
    }

    private fun getThreeHourBaseDateTime(): LocalDateTime {
        val now = LocalDateTime.now()

        return now.truncatedTo(ChronoUnit.HOURS)
            .withHour((now.hour / 3) * 3)
    }
}
