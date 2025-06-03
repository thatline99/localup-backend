package thatline.localup.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping
    fun g(
        @AuthenticationPrincipal userId: Long,
    ): String {
        println("userId=$userId")
        return "test"
    }
}
