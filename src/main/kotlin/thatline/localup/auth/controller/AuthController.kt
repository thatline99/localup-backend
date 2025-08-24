package thatline.localup.auth.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import thatline.localup.auth.exception.*
import thatline.localup.auth.request.KakaoCheckRequest
import thatline.localup.auth.request.KakaoSignupRequest
import thatline.localup.auth.request.SignInRequest
import thatline.localup.auth.request.SignUpRequest
import thatline.localup.auth.service.AuthService
import thatline.localup.common.support.CookieProvider
import org.springframework.beans.factory.annotation.Value

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProvider: CookieProvider,
    @Value("\${app.frontend.base-url}") private val frontendBaseUrl: String,
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
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val accessToken = cookieProvider.findAccessTokenFromRequest(request)

        if (accessToken != null) {
            authService.signOut(accessToken)

            val deletedAccessTokenCookie = cookieProvider.deleteAccessTokenCookie()

            response.addHeader(HttpHeaders.SET_COOKIE, deletedAccessTokenCookie.toString())
        }

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sign-up")
    fun signUp(
        @Valid @RequestBody request: SignUpRequest,
        httpRequest: HttpServletRequest,
    ): ResponseEntity<Void> {

        val baseUrl = "${httpRequest.scheme}://${httpRequest.serverName}:${httpRequest.serverPort}"
        authService.signUp(request.email, request.password, baseUrl)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    /**
     * 이메일 인증 확인 처리
     * GET /api/auth/verify-email?email={email}&token={token}
     *
     * @param email 인증할 이메일 주소
     * @param token 이메일 인증 토큰
     */
    @GetMapping("/verify-email")
    fun verifyEmail(
        @RequestParam email: String,
        @RequestParam token: String,
    ): ResponseEntity<Void> {
        authService.verifyEmail(email)

        val redirectUrl = frontendBaseUrl
        return ResponseEntity.status(HttpStatus.FOUND).
            header(HttpHeaders.LOCATION, redirectUrl).build()
    }

    @PostMapping("/kakao/check")
    fun checkKakao(
        @Valid @RequestBody request: KakaoCheckRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val authToken = authService.checkKakaoUser(request.kakaoId, request.email)

        val accessTokenCookie = cookieProvider.createAccessTokenCookie(authToken.accessToken)
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())

        return ResponseEntity.ok().build()
    }

    @PostMapping("/kakao/signup")
    fun signUpKakao(
        @Valid @RequestBody request: KakaoSignupRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val authToken = authService.signUpKakaoUser(
            request.kakaoId,
            request.email,
            request.name,
            request.profileImage
        )

        val accessTokenCookie = cookieProvider.createAccessTokenCookie(authToken.accessToken)
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())

        return ResponseEntity.status(HttpStatus.CREATED).build()
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


    @ExceptionHandler(AccountDisabledException::class)
    fun handleAccountDisabled(exception: AccountDisabledException): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
}
