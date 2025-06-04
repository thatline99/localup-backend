package thatline.localup.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import thatline.localup.exception.DuplicateEmailException
import thatline.localup.exception.InvalidCredentialsException
import thatline.localup.request.SignInRequest
import thatline.localup.request.SignUpRequest
import thatline.localup.service.AuthService
import thatline.localup.support.CookieProvider

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProvider: CookieProvider,
) {
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val authToken = authService.signIn(request.email, request.password)

        val accessTokenCookie = cookieProvider.createAccessTokenCookie(authToken.accessToken)

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sign-out")
    fun signOut(
        @AuthenticationPrincipal userId: Long,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        authService.signOut(userId)

        val deletedAccessTokenCookie = cookieProvider.deleteAccessTokenCookie()

        response.addHeader(HttpHeaders.SET_COOKIE, deletedAccessTokenCookie.toString())

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<Void> {
        authService.signUp(request.email, request.password)

        return ResponseEntity.ok().build()
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(exception: InvalidCredentialsException): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    // TODO: noah, 추후 error body 정의
    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail(exception: DuplicateEmailException): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }
}
