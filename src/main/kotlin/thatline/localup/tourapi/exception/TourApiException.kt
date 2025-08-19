package thatline.localup.tourapi.exception

import thatline.localup.common.exception.BaseException

open class TourApiException(
    message: String = "TOUR_API",
    cause: Throwable? = null,
    val failedUri: String,
) : BaseException(message, cause)
