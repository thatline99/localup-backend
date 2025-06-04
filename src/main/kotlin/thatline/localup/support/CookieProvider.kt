package thatline.localup.support

import org.springframework.http.ResponseCookie

interface CookieProvider {
    fun createAccessTokenCookie(accessToken: String): ResponseCookie

    fun deleteAccessTokenCookie(): ResponseCookie
}
