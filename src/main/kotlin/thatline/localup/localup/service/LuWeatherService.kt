package thatline.localup.localup.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.common.util.KmaUtil
import thatline.localup.etcapi.code.UltraSrtNcstResponseCode
import thatline.localup.etcapi.restclient.EtcApiRestClient
import thatline.localup.localup.exception.WeatherServiceException
import thatline.localup.localup.response.dto.HourlyWeatherInformation
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class LuWeatherService(
    private val etcApiRestClient: EtcApiRestClient,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun getHourlyWeatherInformationByCoordinates(
        latitude: Double,
        longitude: Double,
    ): HourlyWeatherInformation {
        val baseDateTime = LocalDateTime.now().minusMinutes(BASE_TIME_DELAY_MINUTES).truncatedTo(ChronoUnit.HOURS)
        val baseDate = baseDateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
        val baseTime = baseDateTime.format(DateTimeUtil.DATETIME_FORMATTER_HHmm)

        val (nx, ny) = KmaUtil.convertCoordinatesToGrid(latitude, longitude)

        val response = etcApiRestClient.getUltraSrtNcst(
            pageNo = 1,
            numOfRows = 8,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        )

        if (response.response.header.resultCode != "00") {
            log.warn(
                "resultCode={}, resultMsg={}",
                response.response.header.resultCode,
                response.response.header.resultMsg
            )

            throw WeatherServiceException(
                message = UltraSrtNcstResponseCode.from(response.response.header.resultCode).message
            )
        }

        val body = response.response.body
            ?: throw WeatherServiceException(
                message = "BODY_IS_NULL"
            )

        val map = body.items.item.associate { it.category to it.obsrValue }

        return HourlyWeatherInformation(
            temperature = map.getValue("T1H").toDouble(),
            humidity = map.getValue("REH").toDouble(),
            precipitationType = map.getValue("PTY").toInt(),
            precipitation1h = map.getValue("RN1").toDouble(),
            windSpeed = map.getValue("WSD").toDouble(),
            windDirection = map.getValue("VEC").toInt(),
            windEastWest = map.getValue("UUU").toDouble(),
            windNorthSouth = map.getValue("VVV").toDouble(),
        )
    }

    companion object {
        private const val BASE_TIME_DELAY_MINUTES = 40L
    }
}
