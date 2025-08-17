package thatline.localup.tourapi.exception

class TourApiKorService2AreaBasedList2Exception(
    val resultCode: String,
    val resultMessage: String,
    cause: Throwable? = null,
) : ExternalTourApiException(
    message = resultMessage,
    cause = cause
)
