package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class LastYearSameWeekVisitorStatisticsInformation(
    val updatedDate: LocalDateTime,
    val visitorStatistics: List<VisitorStatistic>,
)
