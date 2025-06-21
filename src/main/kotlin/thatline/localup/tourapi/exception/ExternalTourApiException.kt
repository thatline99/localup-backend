package thatline.localup.tourapi.exception

import thatline.localup.common.exception.BaseException

class ExternalTourApiException(
    cause: Throwable? = null,
) : BaseException("EXTERNAL_TOUR_API", cause)
