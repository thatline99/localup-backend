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
    NODATA_ERROR("03", "NODATA_ERROR", "데이터 없음"),
    HTTP_ERROR("04", "HTTP_ERROR", "HTTP 에러"),
    SERVICETIME_OUT("05", "SERVICETIME_OUT", "서비스 연결 실패"),
    INVALID_REQUEST_PARAMETER_ERROR("10", "INVALID_REQUEST_PARAMETER_ERROR", "잘못된 요청 파라미터"),
    NO_MANDATORY_REQUEST_PARAMETERS_ERROR("11", "NO_MANDATORY_REQUEST_PARAMETERS_ERROR", "필수 파라미터 없음"),
    NO_OPENAPI_SERVICE_ERROR("12", "NO_OPENAPI_SERVICE_ERROR", "해당 API 서비스가 없거나 폐기됨"),
    SERVICE_ACCESS_DENIED_ERROR("20", "SERVICE_ACCESS_DENIED_ERROR", "서비스 접근 거부"),
    TEMPORARILY_DISABLE_SERVICEKEY_ERROR("21", "TEMPORARILY_DISABLE_THE_SERVICEKEY_ERROR", "일시적으로 사용할 수 없는 서비스키"),
    LIMITED_NUMBER_OF_REQUESTS_EXCEEDS("22", "LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR", "요청 제한 횟수 초과"),
    SERVICE_KEY_NOT_REGISTERED("30", "SERVICE_KEY_IS_NOT_REGISTERED_ERROR", "등록되지 않은 서비스키"),
    DEADLINE_EXPIRED("31", "DEADLINE_HAS_EXPIRED_ERROR", "기한 만료된 서비스키"),
    UNREGISTERED_IP("32", "UNREGISTERED_IP_ERROR", "등록되지 않은 IP"),
    UNSIGNED_CALL("33", "UNSIGNED_CALL_ERROR", "서명되지 않은 호출"),
    UNKNOWN_ERROR("99", "UNKNOWN_ERROR", "기타 에러");

    companion object {
        fun from(code: String): UltraSrtNcstResponseCode = entries.find { it.code == code } ?: UNKNOWN_ERROR
    }
}
