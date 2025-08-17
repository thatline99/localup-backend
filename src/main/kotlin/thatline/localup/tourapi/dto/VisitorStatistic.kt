package thatline.localup.tourapi.dto

import java.time.LocalDate

data class VisitorStatistic(
    val date: LocalDate,
    val localVisitors: Int,
    val domesticVisitors: Int,
    val foreignVisitors: Int,
)
