package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class SigunguMainEventInformation(
    val updatedDate: LocalDateTime,
    val sigunguMainEvent: LocationEvent?,
)
