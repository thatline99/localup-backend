package thatline.localup.auth.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class EmailVerificationRedisService(
    private val redisTemplate: StringRedisTemplate,
) {
    companion object {
        private const val EMAIL_VERIFICATION_PREFIX = "email_verification:"
        private const val EXPIRATION_HOURS = 24L
    }

    /**
     * 이메일 인증 토큰을 Redis에 저장합니다.
     *
     * @param email 사용자 이메일
     * @param token 인증 토큰
     */
    fun saveVerificationToken(email: String, token: String) {
        val key = EMAIL_VERIFICATION_PREFIX + email
        val expiration = Duration.ofHours(EXPIRATION_HOURS)
        
        redisTemplate.opsForValue().set(key, token, expiration)
    }

    /**
     * 이메일 인증 토큰을 검증합니다.
     *
     * @param email 사용자 이메일
     * @param token 검증할 토큰
     * @return 토큰이 유효한지 여부
     */
    fun isValidVerificationToken(email: String, token: String): Boolean {
        val key = EMAIL_VERIFICATION_PREFIX + email
        val storedToken = redisTemplate.opsForValue().get(key)
        
        return storedToken != null && storedToken == token
    }

    /**
     * 이메일 인증 완료 후 토큰을 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    fun deleteVerificationToken(email: String) {
        val key = EMAIL_VERIFICATION_PREFIX + email
        redisTemplate.delete(key)
    }

    /**
     * 간단한 랜덤 토큰을 생성합니다.
     *
     * @return 생성된 토큰
     */
    fun generateVerificationToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}