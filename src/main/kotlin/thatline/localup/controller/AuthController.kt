package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.request.SignUpRequest
import thatline.localup.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
//    @PostMapping("/sign-in")
//    fun signIn(
//
//    ): ResponseEntity<Void> {
//        return ResponseEntity.ok().build();
//    }

//    @PostMapping("/sign-out")
//    fun signOut(): ResponseEntity<Void> {
//
//    }

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest
    ): ResponseEntity<Void> {
        authService.signUp(request.email, request.password)

        return ResponseEntity.ok().build()
    }

    // TODO: noah, 이메일 exception 핸들러 추가
}
