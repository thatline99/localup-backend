package thatline.localup.etcapi.dto

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
