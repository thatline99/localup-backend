package thatline.localup.common.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import thatline.localup.etcapi.dto.WeatherInformation
import java.time.Duration


@Configuration
@EnableCaching
class CacheConfiguration(
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun cacheManager(rcf: RedisConnectionFactory): CacheManager {
        val serializer = Jackson2JsonRedisSerializer(objectMapper, WeatherInformation::class.java)

        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(Duration.ofHours(3))

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(rcf)
            .cacheDefaults(redisCacheConfiguration)
            .build()
    }
}
