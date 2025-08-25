package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class SigunguEventInformation(
    val updatedDate: LocalDateTime,
    val sigunguEvents: List<SigunguEvent>,
)
