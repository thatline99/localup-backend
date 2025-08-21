package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class LegalDongSigunguMainEventInformation(
    val updatedDate: LocalDateTime,
    val legalDongSigunguMainEvent: SigunguEventWithDates?,
)
