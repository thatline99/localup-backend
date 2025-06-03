package thatline.localup.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.request.SignInRequest
import thatline.localup.request.SignUpRequest
import thatline.localup.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest
    ): ResponseEntity<Void> {
        authService.signIn(request.email, request.password)

        // TODO: noah, 토큰

        return ResponseEntity.ok().build();
    }

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

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail(exception: DuplicateEmailException): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }
}
