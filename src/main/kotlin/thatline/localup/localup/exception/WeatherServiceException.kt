package thatline.localup.localup.exception

import thatline.localup.common.exception.BaseException

class WeatherServiceException(
    message: String,
    cause: Throwable? = null,
) : BaseException(message, cause)
