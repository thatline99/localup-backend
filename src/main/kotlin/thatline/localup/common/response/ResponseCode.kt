package thatline.localup.common.response

enum class ResponseCode(val code: String, val defaultMessage: String) {
    SUCCESS("SUCCESS", "SUCCESS"),
    FAILURE("FAILURE", "FAILURE"),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED", "이메일 인증을 완료해주세요."),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호는 8자 이상이며, 영문과 숫자를 모두 포함해야 합니다."),
}
