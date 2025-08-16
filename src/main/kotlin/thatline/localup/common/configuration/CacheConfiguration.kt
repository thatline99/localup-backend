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
import thatline.localup.common.constant.CacheObjectName
import thatline.localup.common.util.DateTimeUtil
import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.LastYearSameWeekVisitorStatisticsInformation
import java.time.Duration
import java.time.LocalDateTime
import java.time.YearMonth
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

        // TODO: 네이밍 통일 필요

        // 대시보드, 지난 달 관광지 랭킹 정보 캐시 설정
        val lastMonthlyTouristAttractionRankingInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        LastMonthlyTouristAttractionRankingInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofDays(31))

        // 대시보드, 작년, 같은 주, 지역 방문자 수 통계 정보 캐시 설정
        val lastYearSameWeekVisitorStatisticsInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        LastYearSameWeekVisitorStatisticsInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofDays(7))

        // 대시보드, 날씨 정보 캐시 설정
        val weatherInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        WeatherInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofHours(3))

        val cacheConfigurations = mapOf(
            CacheObjectName.LAST_MONTHLY_TOURIST_ATTRACTION_RANKING_INFORMATION to lastMonthlyTouristAttractionRankingInformationCacheConfiguration,
            CacheObjectName.LAST_YEAR_SAME_WEEK_VISITOR_STATISTICS_INFORMATION to lastYearSameWeekVisitorStatisticsInformationCacheConfiguration,
            CacheObjectName.WEATHER_INFORMATION to weatherInformationCacheConfiguration,
            // 다른 캐시 설정 추가
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfiguration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun lastMonthlyTouristAttractionRankingKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[1] as String

            val savedDateTime = YearMonth.now().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMM)

            "$sigunguCode-$savedDateTime"
        }
    }

    @Bean
    fun lastYearSameWeekVisitorStatistics(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String

            val (startDate, endDate) = DateTimeUtil.getLastYearSameIsoWeekRange()

            val startDateFormat = startDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
            val endDateFormat = endDate.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

            "$sigunguCode-$startDateFormat-$endDateFormat"
        }
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
