package thatline.localup.user.exception

import thatline.localup.common.exception.BaseException

class BusinessNotRegisteredException(
    cause: Throwable? = null,
) : BaseException("BUSINESS_NOT_REGISTERED", cause)
