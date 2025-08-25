package thatline.localup.etcapi.exception.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import thatline.localup.common.response.BaseResponse
import thatline.localup.etcapi.exception.KmaApiException

@RestControllerAdvice
class EtcApiExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(KmaApiException::class)
    fun handleKmaApiException(exception: KmaApiException): ResponseEntity<BaseResponse<Unit>> {
        logger.error(
            "KMA API failed. URI: {}, message: {}",
            exception.failedUri,
            exception.message,
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse.failure())
    }
}
