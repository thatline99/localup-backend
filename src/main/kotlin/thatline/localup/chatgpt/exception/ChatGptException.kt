package thatline.localup.chatgpt.exception

class ChatGptException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)