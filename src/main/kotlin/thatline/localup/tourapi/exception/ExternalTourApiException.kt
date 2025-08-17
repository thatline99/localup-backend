package thatline.localup.tourapi.exception

import thatline.localup.common.exception.BaseException

open class ExternalTourApiException(
    val failedUri: String,
    message: String = "EXTERNAL_TOUR_API",
    cause: Throwable? = null,
) : BaseException(message, cause)
