package thatline.localup.common.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.common.response.BaseResponse
import thatline.localup.common.response.HealthCheckResponse
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class HealthController {
    
    @GetMapping("/health")
    fun health(): ResponseEntity<BaseResponse<HealthCheckResponse>> {
        val response = HealthCheckResponse(
            status = "UP",
            timestamp = LocalDateTime.now(),
            service = "localup-backend"
        )
        
        return ResponseEntity.ok(BaseResponse.success(response))
    }
}