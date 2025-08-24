package thatline.localup.etcapi.dto

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
