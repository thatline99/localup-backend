package thatline.localup.exception

open class BaseException(
    override val message: String
) : RuntimeException(message)
