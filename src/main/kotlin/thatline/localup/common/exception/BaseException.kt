package thatline.localup.common.exception

open class BaseException(
    override val message: String
) : RuntimeException(message)
