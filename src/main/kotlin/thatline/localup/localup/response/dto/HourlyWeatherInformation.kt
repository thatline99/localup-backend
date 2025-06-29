package thatline.localup.localup.response.dto

import java.time.LocalDateTime

/**
 * 시간별 날씨 정보
 *
 * @property temperature 기온 (T1H)
 * @property humidity 습도 (REH)
 * @property precipitationType 강수형태 (PTY, (없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)))
 * @property precipitation1h 1시간 강수량 (RN1)
 * @property windSpeed 풍속 (WSD)
 * @property windDirection 풍향 (VEC)
 * @property windEastWest 동서 바람 성분 (UUU)
 * @property windNorthSouth 남북 바람 성분 (VVV)
 * @property updatedAt 관측일시
 */
data class HourlyWeatherInformation(
    val temperature: Double,
    val humidity: Double,
    val precipitationType: Int,
    val precipitation1h: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val windEastWest: Double,
    val windNorthSouth: Double,
    val updateAt: LocalDateTime,
)
