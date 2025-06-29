package thatline.localup.etcapi.code

/**
 * 기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회 응답 코드
 *
 * @property code        코드
 * @property message     메시지
 * @property description 설명
 *
 * @see <a href="https://www.data.go.kr/data/15084084/openapi.do">공공데이터포털 API 문서</a>
 */
enum class UltraSrtNcstResponseCode(
    val code: String,
    val message: String,
    val description: String,
) {
    NORMAL_SERVICE("00", "NORMAL_SERVICE", "정상"),
    APPLICATION_ERROR("01", "APPLICATION_ERROR", "어플리케이션 에러"),
    DB_ERROR("02", "DB_ERROR", "데이터베이스 에러"),
    NODATA_ERROR("03", "NODATA_ERROR", "데이터없음 에러"),
    HTTP_ERROR("04", "HTTP_ERROR", "HTTP 에러"),
    SERVICETIME_OUT("05", "SERVICETIME_OUT", "서비스 연결실패 에러"),
    INVALID_REQUEST_PARAMETER_ERROR("10", "INVALID_REQUEST_PARAMETER_ERROR", "잘못된 요청 파라메터 에러"),
    NO_MANDATORY_REQUEST_PARAMETERS_ERROR("11", "NO_MANDATORY_REQUEST_PARAMETERS_ERROR", "필수요청 파라메터가 없음"),
    NO_OPENAPI_SERVICE_ERROR("12", "NO_OPENAPI_SERVICE_ERROR", "해당 오픈API서비스가 없거나 폐기됨"),
    SERVICE_ACCESS_DENIED_ERROR("20", "SERVICE_ACCESS_DENIED_ERROR", "서비스 접근거부"),
    TEMPORARILY_DISABLE_THE_SERVICEKEY_ERROR("21", "TEMPORARILY_DISABLE_THE_SERVICEKEY_ERROR", "일시적으로 사용할 수 없는 서비스 키"),
    LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR(
        "22",
        "LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR",
        "서비스 요청제한횟수 초과에러"
    ),
    SERVICE_KEY_IS_NOT_REGISTERED_ERROR("30", "SERVICE_KEY_IS_NOT_REGISTERED_ERROR", "등록되지 않은 서비스키"),
    DEADLINE_HAS_EXPIRED_ERROR("31", "DEADLINE_HAS_EXPIRED_ERROR", "기한만료된 서비스키"),
    UNREGISTERED_IP_ERROR("32", "UNREGISTERED_IP_ERROR", "등록되지 않은 IP"),
    UNSIGNED_CALL_ERROR("33", "UNSIGNED_CALL_ERROR", "서명되지 않은 호출"),
    UNKNOWN_ERROR("99", "UNKNOWN_ERROR", "기타에러");

    companion object {
        fun from(code: String): UltraSrtNcstResponseCode = entries.find { it.code == code } ?: UNKNOWN_ERROR
    }
}
