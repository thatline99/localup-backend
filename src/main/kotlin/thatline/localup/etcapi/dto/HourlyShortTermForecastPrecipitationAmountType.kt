package thatline.localup.etcapi.dto

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
