package thatline.localup.etcapi.exception

import thatline.localup.common.exception.BaseException

class ExternalEtcApiException(
    cause: Throwable? = null,
) : BaseException("EXTERNAL_ETC_API", cause)
