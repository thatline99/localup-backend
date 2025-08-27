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
import thatline.localup.auth.request.KakaoSignUpRequest
import thatline.localup.auth.request.SignInRequest
import thatline.localup.auth.request.SignUpRequest
import thatline.localup.auth.service.AuthService
import thatline.localup.common.support.CookieProvider
import org.springframework.beans.factory.annotation.Value
import thatline.localup.common.response.BaseResponse
import thatline.localup.common.response.ResponseCode
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.data.domain.PageRequest
import thatline.localup.auth.dto.LastLoginInfo

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProvider: CookieProvider,
    @Value("\${app.frontend.base-url}") private val frontendBaseUrl: String,
) {
    @PostMapping("/sign-in")
    fun signIn(
        @Valid @RequestBody request: SignInRequest,
        httpRequest: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val ipAddress = httpRequest.remoteAddr
        val userAgent = httpRequest.getHeader("User-Agent")
        
        val authToken = authService.signIn(request.email, request.password, ipAddress, userAgent)

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
    ): ResponseEntity<BaseResponse<Unit>> {

        val baseUrl = "${httpRequest.scheme}://${httpRequest.serverName}:${httpRequest.serverPort}"
        authService.signUp(request.email, request.password, request.marketingConsent, baseUrl)

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success())
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
        authService.verifyEmail(email, token)

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

    @GetMapping("/last-login")
    fun getLastLoginInfo(@RequestParam email: String): ResponseEntity<BaseResponse<LastLoginInfo?>> {
        val lastLoginInfo = authService.getLastLoginInfo(email)
        return ResponseEntity.ok(BaseResponse.success(lastLoginInfo))
    }
    
    @PostMapping("/kakao/signup")
    fun signUpKakao(
        @Valid @RequestBody request: KakaoSignUpRequest,
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

    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail(exception: DuplicateEmailException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity.badRequest().body(
            BaseResponse.failure(ResponseCode.DUPLICATE_EMAIL)
        )
    }


    @ExceptionHandler(AccountDisabledException::class)
    fun handleAccountDisabled(exception: AccountDisabledException): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
    
    @ExceptionHandler(EmailNotVerifiedException::class)
    fun handleEmailNotVerified(exception: EmailNotVerifiedException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            BaseResponse.failure(ResponseCode.EMAIL_NOT_VERIFIED)
        )
    }
    
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exception: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Unit>> {
        val errors = exception.bindingResult.fieldErrors
        
        // 비밀번호 유효성 검사 실패 확인
        val passwordError = errors.find { it.field == "password" }
        if (passwordError != null) {
            return ResponseEntity.badRequest().body(
                BaseResponse.failure(ResponseCode.INVALID_PASSWORD)
            )
        }
        
        // 다른 유효성 검사 실패
        val message = errors.firstOrNull()?.defaultMessage ?: "입력값이 올바르지 않습니다."
        return ResponseEntity.badRequest().body(
            BaseResponse.failure(ResponseCode.FAILURE, message)
        )
    }
}
