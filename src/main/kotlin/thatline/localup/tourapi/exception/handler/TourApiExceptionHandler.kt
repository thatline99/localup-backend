package thatline.localup.tourapi.exception.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import thatline.localup.common.response.BaseResponse
import thatline.localup.tourapi.exception.TourApiKorService2AreaBasedList2Exception

@RestControllerAdvice
class TourApiExceptionHandler {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(TourApiKorService2AreaBasedList2Exception::class)
    fun handleTourApiKorService2AreaBasedList2Exception(
        exception: TourApiKorService2AreaBasedList2Exception,
    ): ResponseEntity<BaseResponse<Unit>> {
        log.error(
            "Tour API KorService2 AreaBasedList2 failed. URI: {}, resultCode: {}, resultMessage: {}",
            exception.failedUri,
            exception.resultCode,
            exception.resultMessage
        )

        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(BaseResponse.failure())
    }
}
