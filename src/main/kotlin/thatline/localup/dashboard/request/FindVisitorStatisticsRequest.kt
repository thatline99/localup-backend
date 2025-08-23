package thatline.localup.dashboard.request

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class FindVisitorStatisticsRequest(
    @field:NotNull
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val startDate: LocalDate,

    @field:NotNull
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val endDate: LocalDate,
) {
//    TODO-noah: 범위 유호성 검사 필요
//    @AssertTrue
//    fun isYearInRange(): Boolean {
//        return startDate.year in 2018..2024 && endDate.year in 2018..2024
//    }
//
//    @AssertTrue
//    fun isStartNotBeforeEnd(): Boolean {
//        return !startDate.isBefore(endDate)
//    }
}
