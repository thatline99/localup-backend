package thatline.localup.user.exception

import thatline.localup.common.exception.BaseException

class BusinessAlreadyRegisteredException(
    cause: Throwable? = null,
) : BaseException("BUSINESS_ALREADY_REGISTERED", cause)
