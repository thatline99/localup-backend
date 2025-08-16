package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class LastMonthlyTouristAttractionRankingInformation(
    val updatedDate: LocalDateTime,
    val lastMonthlyTouristAttractionRankingList: List<LastMonthlyTouristAttractionRanking>,
)
