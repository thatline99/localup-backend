package thatline.localup.support

import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import thatline.localup.constant.Environment
import thatline.localup.property.TokenProperty

@Profile(Environment.LOCAL)
@Component
class LocalCookieProvider(
    private val tokenProperty: TokenProperty,
) : CookieProvider {
    override fun createAccessTokenCookie(accessToken: String): ResponseCookie {
        return ResponseCookie.from(tokenProperty.accessToken.name, accessToken)
            .path("/")
            .httpOnly(true)
            .sameSite("Lax")
            .maxAge(tokenProperty.accessToken.expirationSeconds)
            .build()
    }

    override fun deleteAccessTokenCookie(): ResponseCookie {
        return ResponseCookie.from(tokenProperty.accessToken.name, "")
            .path("/")
            .httpOnly(true)
            .sameSite("Lax")
            .maxAge(0)
            .build()
    }
}
