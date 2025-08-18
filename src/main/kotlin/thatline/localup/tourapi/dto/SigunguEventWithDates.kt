package thatline.localup.tourapi.dto

import java.time.LocalDate

data class SigunguEventWithDates(
    val contentTypeId: String,
    val contentId: String,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val zipCode: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val telephone: String,
    val originalImageUrl: String,
    val thumbnailImageUrl: String,
)
