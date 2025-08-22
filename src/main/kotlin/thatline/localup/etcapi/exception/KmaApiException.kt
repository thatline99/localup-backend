package thatline.localup.etcapi.exception

import thatline.localup.common.exception.BaseException

// KMA(Korea Meteorological Administration, 기상청) 예외
open class KmaApiException(
    message: String = "KMA_API",
    cause: Throwable? = null,
    val failedUri: String,
) : BaseException(message, cause)
