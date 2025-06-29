package thatline.localup.user.exception

import thatline.localup.common.exception.BaseException

class UserNotFoundException(
    cause: Throwable? = null,
) : BaseException("USER_NOT_FOUND", cause)
