package thatline.localup.etcapi.dto

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
