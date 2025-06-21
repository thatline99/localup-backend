package thatline.localup.common.support

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseCookie

interface CookieProvider {
    fun createAccessTokenCookie(accessToken: String): ResponseCookie

    fun findAccessTokenFromRequest(request: HttpServletRequest): String?

    fun deleteAccessTokenCookie(): ResponseCookie
}
