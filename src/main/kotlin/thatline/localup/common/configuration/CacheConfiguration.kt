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
import thatline.localup.etcapi.dto.ShortTermForecastInformation
import thatline.localup.tourapi.dto.LastMonthlyTouristAttractionRankingInformation
import thatline.localup.tourapi.dto.OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation
import thatline.localup.tourapi.dto.SigunguMainEventInformation
import thatline.localup.tourapi.dto.VisitorStatisticsInformation
import thatline.localup.dashboard.dto.WeatherInsightResponse
import thatline.localup.dashboard.dto.BusinessMetricsResponse
import java.time.Duration
import java.time.LocalDate
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

        // 대시보드, 시군구 단기 예보 정보 캐시 설정
        val shortTermForecastsInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        ShortTermForecastInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofHours(3))

        // 대시보드, 시군구 방문자 수 통계 정보 캐시 설정
        val visitorStatisticsInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        VisitorStatisticsInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofDays(7))

        // 개선 전

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

        // 대시보드, 시군구 메인 이벤트 정보 캐시 설정
        val sigunguMainEventInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        SigunguMainEventInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofDays(1))

        // 대시보드, 시군구 오늘 날짜 ~ 월 마지막 일 축제/공연/행사 정보 캐시 설정
        val ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformationCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        OngoingOrUpComingSigunguEventsFromTodayToMonthEndInformation::class.java
                    )
                )
            )
            .entryTtl(Duration.ofDays(1))

        // AI 날씨 인사이트 캐시 설정 (3시간)
        val weatherInsightCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        WeatherInsightResponse::class.java
                    )
                )
            )
            .entryTtl(Duration.ofHours(3))

        // AI 비즈니스 메트릭 캐시 설정 (1시간)
        val businessMetricsCacheConfiguration = defaultCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(
                        objectMapper,
                        BusinessMetricsResponse::class.java
                    )
                )
            )
            .entryTtl(Duration.ofHours(1))

        val cacheConfigurations = mapOf(
            CacheObjectName.SHORT_TERM_FORECAST_INFORMATION to shortTermForecastsInformationCacheConfiguration,
            CacheObjectName.VISITOR_STATISTICS_INFORMATION to visitorStatisticsInformationCacheConfiguration,

            CacheObjectName.LAST_MONTHLY_TOURIST_ATTRACTION_RANKING_INFORMATION to lastMonthlyTouristAttractionRankingInformationCacheConfiguration,
            CacheObjectName.SIGUNGU_MAIN_EVENT_INFORMATION to sigunguMainEventInformationCacheConfiguration,
            CacheObjectName.ONGOING_OR_UPCOMING_SIGUNGU_EVENTS_FROM_TODAY_TO_MONTH_END_INFORMATION to ongoingOrUpComingSigunguEventsFromTodayToMonthEndInformationCacheConfiguration,
            "weatherInsight" to weatherInsightCacheConfiguration,
            "businessMetrics" to businessMetricsCacheConfiguration
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfiguration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun shortTermForecastInformationKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String
            val savedDateTime = (params[1] as LocalDateTime)
                .truncatedTo(ChronoUnit.HOURS)
                .withHour((params[1] as LocalDateTime).hour / 3 * 3)

            "$sigunguCode-${savedDateTime.format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMddHHmm)}"
        }
    }

    @Bean
    fun visitorStatisticsInformationKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String
            val startDate = (params[1] as LocalDate).format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)
            val endDate = (params[2] as LocalDate).format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

            "$sigunguCode-$startDate-$endDate"
        }
    }

    // 개선 전

    @Bean
    fun lastMonthlyTouristAttractionRankingKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[1] as String

            val savedDateTime = YearMonth.now().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMM)

            "$sigunguCode-$savedDateTime"
        }
    }

    @Bean
    fun sigunguMainEventKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String
            val latitude = params[1] as Double
            val longitude = params[2] as Double

            val savedDateTime = LocalDateTime.now().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

            "$sigunguCode-$latitude-$longitude-$savedDateTime"
        }
    }

    @Bean
    fun ongoingOrUpComingSigunguEventsFromTodayToMonthEndKeyGenerator(): KeyGenerator {
        return KeyGenerator { _, _, params ->
            val sigunguCode = params[0] as String

            val savedDateTime = LocalDateTime.now().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMdd)

            "$sigunguCode-$savedDateTime"
        }
    }
}
