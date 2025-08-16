package thatline.localup.common.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.etcapi.dto.WeatherInformation
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


@Configuration
@EnableCaching
class CacheConfiguration(
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        // 기본 캐시 설정
        val defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .disableCachingNullValues()

        // 대시보드, 날씨 캐시 설정
        val weatherInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(objectMapper, WeatherInformation::class.java)
                )
            )
            .entryTtl(Duration.ofHours(3))

        val cacheConfigurations = mapOf(
            "weatherInformation" to weatherInformationCacheConfiguration,
            // 다른 캐시 설정 추가
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfiguration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun weatherInformationKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String

            val now = LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS)

            val savedDateTime = now.withHour((now.hour / 3) * 3)
                .format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMddHHmm)

            "$sigunguCode-$savedDateTime"
        }
    }
}
