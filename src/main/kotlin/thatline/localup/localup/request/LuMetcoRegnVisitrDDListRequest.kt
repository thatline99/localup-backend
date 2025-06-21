package thatline.localup.localup.request

import java.time.LocalDate

data class LuMetcoRegnVisitrDDListRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val areaCode: String,
)
