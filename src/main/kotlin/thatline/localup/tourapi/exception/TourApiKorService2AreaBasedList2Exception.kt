package thatline.localup.tourapi.exception

class TourApiKorService2AreaBasedList2Exception(
    failedUri: String,
    val resultCode: String,
    val resultMessage: String,
    cause: Throwable? = null,
) : TourApiException(
    failedUri = failedUri,
    message = resultMessage,
    cause = cause
)
