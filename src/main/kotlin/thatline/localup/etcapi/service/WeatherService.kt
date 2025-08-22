package thatline.localup.etcapi.service

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import thatline.localup.common.constant.CacheKeyGeneratorName
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.constant.TourApi
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.etcapi.dto.DailyWeather
import thatline.localup.etcapi.dto.WeatherCondition
import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.etcapi.response.GetVilageFcstResponse
import thatline.localup.etcapi.restclient.EtcApiRestClient
import thatline.localup.localup.exception.WeatherServiceException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class WeatherService(
    private val etcApiRestClient: EtcApiRestClient,
) {
    private val log = LoggerFactory.getLogger(this::class.java)


//    @Deprecated("대체")
//    @Cacheable(
//        cacheNames = [CacheObjectName.WEATHER_INFORMATION],
//        keyGenerator = CacheKeyGeneratorName.WEATHER_INFORMATION,
//        sync = true
//    )
//    fun getThreeDayWeatherSummaries(
//        sigunguCode: String,
//    ): WeatherInformation {
//        val baseDateTime = LocalDateTime.now()
//            .minusMinutes(10) // 단기예보조회, API 제공 시간 10분 보정
//            .truncatedTo(ChronoUnit.HOURS)
//        val baseDate = baseDateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
//        val baseTime = "0200"
//
//        // TODO: 로직 개선 필요
//        val tourApiArea = TourApi.getTourApiAreaBySigunguCode(sigunguCode)
//            ?: throw IllegalArgumentException()
//
//        val response = etcApiRestClient.getVilageFcst(
//            pageNo = 1,
//            numOfRows = 834, // 기준 날짜(254개) + 내일(290개) + 모레(290개)
//            baseDate = baseDate,
//            baseTime = baseTime,
//            nx = tourApiArea.nx,
//            ny = tourApiArea.ny,
//        )
//
//        val body = response.response.body
//            ?: throw WeatherServiceException(message = "BODY_IS_NULL")
//
//        val dailyWeatherList = body.items.item
//            .groupBy { it.fcstDate }
//            .toSortedMap().entries.map { (date, items) ->
//                val precipitationTypeValues = items
//                    .filter { it.category == "PTY" }
//                    .map { it.fcstValue }
//
//                val skyConditionValues = items
//                    .filter { it.category == "SKY" }
//                    .map { it.fcstValue }
//
//                val condition =
//                    if (precipitationTypeValues.any { it != "0" }) {
//                        determinePrecipitationCondition(precipitationTypeValues)
//                    } else {
//                        determineSkyCondition(skyConditionValues)
//                    }
//
//                val minimumTemperature = items.first { it.category == "TMN" }.fcstValue.toDouble()
//                val maximumTemperature = items.first { it.category == "TMX" }.fcstValue.toDouble()
//
//                DailyWeather(
//                    date = LocalDate.parse(date, DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd),
//                    condition = condition,
//                    minimumTemperature = minimumTemperature,
//                    maximumTemperature = maximumTemperature,
//                )
//            }
//
//        return WeatherInformation(
//            updatedDate = getThreeHourBaseDateTime(),
//            dailyWeatherList = dailyWeatherList,
//        )
//    }

    fun findShortTermForecast(
        sigunguCode: String,
        baseDate: String = LocalDateTime.now().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd),
        baseTime: String = "0200",
    ): List<ShortTermForecast> {
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

data class ShortTermForecast(
    val date: LocalDate,

    val dailyMinimumTemperature: Double?, // TMN (일 최저기온, ℃)
    val dailyMaximumTemperature: Double?, // TMX (일 최고기온, ℃)

    val hourlyShortTermForecasts: List<HourlyShortTermForecast>,
)

data class HourlyShortTermForecast(
    val time: LocalTime,                                                        // 예보 시각 (fcstTime, HHmm) V
    val precipitationProbability: Int?,                                         // POP (강수확률, %)
    val precipitationType: HourlyShortTermForecastPrecipitationType?,           // PTY (강수형태)
    val precipitationAmount: HourlyShortTermForecastPrecipitationAmountType?,   // PCP (1시간 강수량, mm) 1 강수없음 파싱 실패 시 V
    val humidity: Int?,                                                         // REH (습도, %)
    val snowfallAmount: HourlyShortTermForecastSnowfallAmountType?,             // SNO (1시간 신적설, cm) 숫자 파싱 실패 시 V
    val skyCondition: HourlyShortTermForecastSkyConditionType?,                 // SKY (하늘상태)
    val temperature: Double?,                                                   // TMP (1시간 기온, ℃)
    val windUComponent: Double?,                                                // UUU (풍속 동서성분, m/s)
    val windVComponent: Double?,                                                // VVV (풍속 남북성분, m/s)
    val waveHeight: Int?,                                                       // WAV (파고, M)
    val windDirection: Int?,                                                    // VEC (풍향, deg)
    val windSpeed: Double?,                                                     // WSD (풍속, m/s)
    val windSpeedType: HourlyShortTermForecastWindSpeedType?,                   // 추가, 풍속 타입
)

// NOTE-noah: 초단기와 다름
enum class HourlyShortTermForecastPrecipitationType(
    val code: Int,
    val label: String,
) {
    NONE(
        code = 0,
        label = "없음"
    ),
    RAIN(
        code = 1,
        label = "비"
    ),
    RAIN_AND_SNOW(
        code = 2,
        label = "비/눈"
    ),
    SHOWER(
        code = 3,
        label = "소나기"
    );

    companion object {
        fun fromCodeOrNull(code: String?): HourlyShortTermForecastPrecipitationType? =
            when (code?.trim()?.toIntOrNull()) {
                0 -> NONE
                1 -> RAIN
                2 -> RAIN_AND_SNOW
                3 -> SHOWER
                else -> null
            }
    }
}

enum class HourlyShortTermForecastPrecipitationAmountType(
    val code: Int,
    val label: String,
    val criteria: String,
) {
    NONE(
        code = 0,
        label = "없음",
        criteria = "강수 없음"
    ),
    LIGHT(
        code = 1,
        label = "약한 비",
        criteria = "시간당 3mm 미만"
    ),
    MODERATE(
        code = 2,
        label = "보통 비",
        criteria = "시간당 3mm 이상 15mm 미만"
    ),
    HEAVY(
        code = 3,
        label = "강한 비",
        criteria = "시간당 15mm 이상"
    );

    companion object {
        fun fromCodeOrNull(code: String?): HourlyShortTermForecastPrecipitationAmountType? =
            when (code?.trim()) {
                "강수없음" -> NONE
                "0" -> NONE
                "1" -> LIGHT
                "2" -> MODERATE
                "3" -> HEAVY
                else -> null
            }
    }
}

enum class HourlyShortTermForecastSnowfallAmountType(
    val code: Int,
    val label: String,
    val criteria: String,
) {
    NONE(
        code = 0,
        label = "없음",
        criteria = "적설 없음"
    ),
    NORMAL(
        code = 1,
        label = "보통 눈",
        criteria = "시간당 1cm 미만"
    ),
    HEAVY(
        code = 2,
        label = "많은 눈",
        criteria = "시간당 1cm 이상"
    );

    companion object {
        fun fromCodeOrNull(code: String?): HourlyShortTermForecastSnowfallAmountType? =
            when (code?.trim()) {
                "적설없음" -> NONE
                "0" -> NORMAL
                "1" -> HEAVY
                else -> null
            }
    }
}

enum class HourlyShortTermForecastSkyConditionType(
    val code: Int,
    val label: String,
) {
    SUNNY(
        code = 1,
        label = "맑음"
    ),
    PARTLY_CLOUDY(
        code = 3,
        label = "구름 많음"
    ),
    CLOUDY(
        code = 4,
        label = "흐림"
    );

    companion object {
        fun fromCodeOrNull(code: String?): HourlyShortTermForecastSkyConditionType? =
            when (code?.trim()?.toIntOrNull()) {
                1 -> SUNNY
                3 -> PARTLY_CLOUDY
                4 -> CLOUDY
                else -> null
            }
    }
}

enum class HourlyShortTermForecastWindSpeedType(
    val code: Int,
    val label: String,
    val criteria: String,
) {
    WEAK(
        code = 1,
        label = "약한 바람",
        criteria = "4m/s 미만"
    ),
    MODERATE(
        code = 2,
        label = "약간 강한 바람",
        criteria = "4m/s 이상 9m/s 미만"
    ),
    STRONG(
        code = 3,
        label = "강한 바람",
        criteria = "9m/s 이상"
    );

    companion object {
        fun fromValue(speed: Double): HourlyShortTermForecastWindSpeedType = when {
            speed < 4.0 -> WEAK
            speed < 9.0 -> MODERATE
            else -> STRONG
        }
    }
}
