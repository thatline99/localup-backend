package thatline.localup.common.response

enum class ResponseCode(val code: String, val defaultMessage: String) {
    SUCCESS("SUCCESS", "SUCCESS"),
    FAILURE("FAILURE", "FAILURE"),
}
