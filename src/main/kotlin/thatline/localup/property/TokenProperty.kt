package thatline.localup.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "token")
@Validated
data class TokenProperty(
    val accessToken: AccessToken,
) {
    data class AccessToken(
        var name: String,
        val expirationSeconds: Int,
    )
}
