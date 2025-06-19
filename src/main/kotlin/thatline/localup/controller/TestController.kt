package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

// Hello, World!

@Controller
class TestController {

    @PostMapping("/TeSt")
    fun test(): ResponseEntity<Void> {
        println("Log: Test")

        return ResponseEntity.ok().build()
    }

    private fun NotTTHING() {
        println("Log: DELETE")
    }
}
