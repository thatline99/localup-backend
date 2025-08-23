package thatline.localup.etcapi.dto

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
