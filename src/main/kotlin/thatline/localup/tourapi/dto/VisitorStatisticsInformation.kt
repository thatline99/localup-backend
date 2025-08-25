package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class VisitorStatisticsInformation(
    val updatedDate: LocalDateTime,
    val visitorStatistics: List<VisitorStatistic>,
)
