package thatline.localup.common.configuration

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CacheConfiguration {

    // TODO-noah: 추후 레디스 캐시로 개선
    @Bean
    fun cacheManager(): CacheManager {
        // 스프링 기본 메모리 캐시 사용
        return ConcurrentMapCacheManager("weatherCache")
    }
}
