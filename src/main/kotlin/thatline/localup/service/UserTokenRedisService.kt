package thatline.localup.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import thatline.localup.property.TokenProperty
import java.time.Duration

@Service
class UserTokenRedisService(
    private val tokenProperty: TokenProperty,
    private val redisTemplate: StringRedisTemplate,
) {
    fun save(accessToken: String, userId: String) {
        val expiration = Duration.ofSeconds(tokenProperty.accessToken.expirationSeconds)

        redisTemplate.opsForValue().set(accessToken, userId, expiration)
    }

    fun findUserIdByAccessToken(accessToken: String): String? {
        return redisTemplate.opsForValue().get(accessToken)
    }

    fun deleteByAccessToken(accessToken: String) {
        redisTemplate.delete(accessToken)
    }
}
