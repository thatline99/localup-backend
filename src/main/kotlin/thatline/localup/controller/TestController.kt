package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

// Hello, World!

@Controller
class TestController {

    /**
     * Handles HTTP POST requests to the "/TeSt" endpoint.
     *
     * Returns an HTTP 200 OK response with no content.
     */
    @PostMapping("/TeSt")
    fun test(): ResponseEntity<Void> {
        println("Log: Test")

        return ResponseEntity.ok().build()
    }

    /**
     * Logs a "DELETE" message to the console.
     */
    private fun NotTTHING() {
        println("Log: DELETE")
    }
}
