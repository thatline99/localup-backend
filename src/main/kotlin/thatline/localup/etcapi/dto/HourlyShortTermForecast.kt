package thatline.localup.etcapi.dto

import java.time.LocalTime

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
