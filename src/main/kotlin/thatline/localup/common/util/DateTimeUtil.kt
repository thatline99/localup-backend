package thatline.localup.common.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.WeekFields
import kotlin.math.min

object DateTimeUtil {
    val DATETIME_FORMATTER_yyyyMM: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
    val DATETIME_FORMATTER_yyyyMMdd: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val DATETIME_FORMATTER_yyyyMMddHHmm: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    val DATETIME_FORMATTER_HHmm: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmm")

    // LocalDate 기준, 작년 같은 ISO 주차 범위 반환
    fun getLastYearSameIsoWeekRange(localDate: LocalDate = LocalDate.now()): Pair<LocalDate, LocalDate> {
        val isoWeekFields = WeekFields.ISO
        val currentWeekNumber = localDate.get(isoWeekFields.weekOfWeekBasedYear())
        val lastYearWeekYear = localDate.get(isoWeekFields.weekBasedYear()) - 1
        val lastYearMaxWeek = LocalDate.of(lastYearWeekYear, 12, 28)
            .get(isoWeekFields.weekOfWeekBasedYear())
        val adjustedWeekNumber = min(currentWeekNumber, lastYearMaxWeek)

        return isoWeekRangeOf(lastYearWeekYear, adjustedWeekNumber)
    }

    // ISO 주차 범위, 월 ~ 일 반환
    private fun isoWeekRangeOf(weekBasedYear: Int, week: Int): Pair<LocalDate, LocalDate> {
        val startLocalDate = LocalDate.of(weekBasedYear, 1, 4)
            .with(WeekFields.ISO.weekOfWeekBasedYear(), week.toLong())
            .with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.value.toLong())
        val endLocalDate = startLocalDate.plusDays(6)

        return startLocalDate to endLocalDate
    }
}
