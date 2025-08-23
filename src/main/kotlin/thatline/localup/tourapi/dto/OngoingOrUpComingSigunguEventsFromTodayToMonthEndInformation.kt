package thatline.localup.tourapi.dto

import java.time.LocalDateTime

data class OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation(
    val updatedDate: LocalDateTime,
    val sigunguEvents: List<LocationEvent>,
)
