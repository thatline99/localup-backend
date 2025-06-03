package thatline.localup.support

import jakarta.servlet.http.Cookie
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import thatline.localup.constant.Environment
import thatline.localup.property.TokenProperty

@Profile(Environment.LOCAL)
@Component
class LocalCookieProvider(
    private val tokenProperty: TokenProperty,
) : CookieProvider {
    override fun createAccessTokenCookie(accessToken: String): Cookie {
        return Cookie(tokenProperty.accessToken.name, accessToken).apply {
            isHttpOnly = true
            path = "/"
            maxAge = tokenProperty.accessToken.expirationSeconds
        }
    }
}
