package thatline.localup.localup.request

import java.time.LocalDate

data class LuLocgoRegnVisitrDDListRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val signguCode: String,
)
