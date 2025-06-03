package thatline.localup.support

import jakarta.servlet.http.Cookie

interface CookieProvider {
    fun createAccessTokenCookie(accessToken: String): Cookie
}
