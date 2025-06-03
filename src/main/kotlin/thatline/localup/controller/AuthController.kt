package thatline.localup.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
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
        @RequestBody request: SignInRequest,
        response: HttpServletResponse
    ): ResponseEntity<Void> {
        val accessToken = authService.signIn(request.email, request.password)

        // TODO: noah, 쿠키 생성, 환경에 따라 분리, 메서드 분리
        val cookie = Cookie("accessToken", accessToken)
        cookie.isHttpOnly = true
        cookie.maxAge = 60 * 60 * 24
        cookie.path = "/"
        // cookie.secure = true

        response.addCookie(cookie)

        return ResponseEntity.ok().build()
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
