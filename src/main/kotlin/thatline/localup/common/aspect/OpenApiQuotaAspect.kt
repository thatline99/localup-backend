package thatline.localup.common.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import thatline.localup.common.annotation.OpenApiQuota
import thatline.localup.common.exception.OpenApiQuotaExceededException
import java.util.concurrent.TimeUnit

@Aspect
@Component
class OpenApiQuotaAspect(
    private val redisTemplate: StringRedisTemplate,
) {
    @Around("@annotation(openApiQuota)")
    fun checkQuota(proceedingJoinPoint: ProceedingJoinPoint, openApiQuota: OpenApiQuota): Any? {
        val key = "openApi:${openApiQuota.name}:${openApiQuota.apiWindow.getKey()}"

        val currentCount = redisTemplate.opsForValue().increment(key) ?: 1L

        if (currentCount == 1L) {
            redisTemplate.expire(key, openApiQuota.apiWindow.ttl, TimeUnit.SECONDS)
        }

        if (currentCount > openApiQuota.limit) {
            throw OpenApiQuotaExceededException(
                name = openApiQuota.name,
                limit = openApiQuota.limit,
                currentCount = currentCount,
            )
        }

        return proceedingJoinPoint.proceed()
    }
}
