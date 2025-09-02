package thatline.localup.dashboard.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import thatline.localup.chatgpt.dto.ChatGptRequest
import thatline.localup.chatgpt.dto.Message
import thatline.localup.chatgpt.restclient.ChatGptRestClient
import thatline.localup.dashboard.dto.*
import thatline.localup.etcapi.dto.ShortTermForecast
import thatline.localup.user.service.UserService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import thatline.localup.common.constant.CacheObjectName

@Service
class AiInsightService(
    private val chatGptRestClient: ChatGptRestClient,
    private val userService: UserService,
    private val objectMapper: ObjectMapper,
    @Value("\${openai.model:gpt-4o-mini}")
    private val model: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(AiInsightService::class.java)
        private const val WEATHER_INSIGHT_MAX_TOKENS = 200
        private const val BUSINESS_METRICS_MAX_TOKENS = 500
        private const val WEATHER_INSIGHT_CACHE = "weatherInsight"
        private const val BUSINESS_METRICS_CACHE = "businessMetrics"
    }

    @Cacheable(
        value = ["weatherInsight"],
        key = "#userId + '-' + T(java.time.LocalDate).now().toString() + '-' + #request.weatherData.hashCode()",
        condition = "#request.businessInfo != null"
    )
    fun generateWeatherInsight(
        request: WeatherInsightRequest,
        userId: String
    ): WeatherInsightResponse {
        try {
            // 사용자의 실제 사업정보 가져오기
            val businessInfo = request.businessInfo ?: try {
                val businessDto = userService.findBusiness(userId)
                BusinessInfo(
                    name = businessDto.name,
                    address = businessDto.address,
                    category = businessDto.type,
                    description = businessDto.description
                )
            } catch (e: Exception) {
                log.warn("사업정보 조회 실패: ${e.message}")
                null
            }

            val weatherSummary = summarizeWeather(request.weatherData)
            val prompt = buildWeatherInsightPrompt(weatherSummary, businessInfo)
            
            val chatRequest = ChatGptRequest(
                model = model,
                messages = listOf(
                    Message(
                        role = "system",
                        content = "당신은 지역 사업자를 위한 날씨 기반 비즈니스 컨설턴트입니다. 간결하고 실용적인 조언을 제공합니다."
                    ),
                    Message(role = "user", content = prompt)
                ),
                temperature = 0.7,
                maxTokens = WEATHER_INSIGHT_MAX_TOKENS
            )

            val response = chatGptRestClient.chat(chatRequest)
            val insight = response.choices.firstOrNull()?.message?.content 
                ?: generateDefaultWeatherInsight(request.weatherData, businessInfo)

            return WeatherInsightResponse(
                insight = insight.trim(),
                generatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        } catch (e: Exception) {
            log.error("날씨 인사이트 생성 실패", e)
            return WeatherInsightResponse(
                insight = generateDefaultWeatherInsight(request.weatherData, request.businessInfo),
                generatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        }
    }

    @Cacheable(
        value = ["businessMetrics"],
        key = "#userId + '-' + T(java.time.LocalDate).now().toString() + '-' + #request.weatherData?.hashCode()",
        condition = "#request.businessInfo != null || #request.historicalData != null"
    )
    fun generateBusinessMetrics(
        request: BusinessMetricsRequest,
        userId: String
    ): BusinessMetricsResponse {
        try {
            val businessInfo = request.businessInfo ?: try {
                val businessDto = userService.findBusiness(userId)
                BusinessInfo(
                    name = businessDto.name,
                    address = businessDto.address,
                    category = businessDto.type,
                    description = businessDto.description
                )
            } catch (e: Exception) {
                log.warn("사업정보 조회 실패: ${e.message}")
                null
            }

            val prompt = buildBusinessMetricsPrompt(request, businessInfo)
            
            val chatRequest = ChatGptRequest(
                model = model,
                messages = listOf(
                    Message(
                        role = "system",
                        content = """
                            당신은 지역 사업자를 위한 AI 비즈니스 분석가입니다.
                            날씨와 과거 데이터를 기반으로 매출과 방문객을 예측합니다.
                            JSON 형식으로만 응답하세요.
                        """.trimIndent()
                    ),
                    Message(role = "user", content = prompt)
                ),
                temperature = 0.5,
                maxTokens = BUSINESS_METRICS_MAX_TOKENS
            )

            val response = chatGptRestClient.chat(chatRequest)
            val content = response.choices.firstOrNull()?.message?.content
            
            return if (!content.isNullOrBlank()) {
                parseMetricsResponse(content) ?: generateDefaultMetrics(request)
            } else {
                generateDefaultMetrics(request)
            }
        } catch (e: Exception) {
            log.error("비즈니스 메트릭 생성 실패", e)
            return generateDefaultMetrics(request)
        }
    }

    private fun summarizeWeather(forecasts: List<ShortTermForecast>): String {
        if (forecasts.isEmpty()) return "날씨 데이터 없음"
        
        val today = forecasts.firstOrNull()
        val tomorrow = forecasts.getOrNull(1)
        
        val summary = StringBuilder()
        
        today?.let { forecast ->
            val maxRain = forecast.hourlyShortTermForecasts
                .mapNotNull { it.precipitationProbability }
                .maxOrNull() ?: 0
            
            summary.append("오늘: ")
            summary.append("${forecast.dailyMinimumTemperature}~${forecast.dailyMaximumTemperature}°C")
            if (maxRain > 30) {
                summary.append(", 강수확률 ${maxRain}%")
            }
        }
        
        tomorrow?.let { forecast ->
            val maxRain = forecast.hourlyShortTermForecasts
                .mapNotNull { it.precipitationProbability }
                .maxOrNull() ?: 0
            
            summary.append(" / 내일: ")
            summary.append("${forecast.dailyMinimumTemperature}~${forecast.dailyMaximumTemperature}°C")
            if (maxRain > 30) {
                summary.append(", 강수확률 ${maxRain}%")
            }
        }
        
        return summary.toString()
    }

    private fun buildWeatherInsightPrompt(weatherSummary: String, businessInfo: BusinessInfo?): String {
        val businessContext = businessInfo?.let {
            """
            사업정보:
            - 업종: ${it.category ?: "미분류"}
            - 위치: ${it.address ?: "미등록"}
            - 설명: ${it.description ?: "없음"}
            """.trimIndent()
        } ?: "사업정보 없음"

        return """
            날씨: $weatherSummary
            $businessContext
            
            위 정보를 바탕으로 오늘과 내일의 날씨가 사업에 미칠 영향을 50자 이내로 간단히 분석하고 대응 방안을 제시하세요.
        """.trimIndent()
    }

    private fun buildBusinessMetricsPrompt(
        request: BusinessMetricsRequest,
        businessInfo: BusinessInfo?
    ): String {
        val weatherSummary = request.weatherData?.let { summarizeWeather(it) } ?: "날씨 정보 없음"
        val historicalData = request.historicalData?.let {
            """
            과거 데이터:
            - 평균 매출: ${it.averageRevenue ?: 0}원
            - 평균 방문객: ${it.averageVisitors ?: 0}명
            - 피크 시간: ${it.peakHours?.joinToString(", ") ?: "없음"}시
            """.trimIndent()
        } ?: "과거 데이터 없음"

        return """
            날씨: $weatherSummary
            업종: ${businessInfo?.category ?: "미분류"}
            $historicalData
            
            위 정보를 바탕으로 오늘과 내일의 예상 매출과 방문객을 예측하세요.
            다음 JSON 형식으로 응답하세요:
            {
              "todayRevenue": {"value": 숫자, "change": 퍼센트, "trend": "up/down/stable", "confidence": 0-1},
              "todayVisitors": {"value": 숫자, "change": 퍼센트, "trend": "up/down/stable", "confidence": 0-1},
              "tomorrowRevenue": {"value": 숫자, "change": 퍼센트, "trend": "up/down/stable", "confidence": 0-1},
              "tomorrowVisitors": {"value": 숫자, "change": 퍼센트, "trend": "up/down/stable", "confidence": 0-1},
              "recommendations": ["추천1", "추천2", "추천3"]
            }
        """.trimIndent()
    }

    private fun parseMetricsResponse(content: String): BusinessMetricsResponse? {
        return try {
            val jsonNode = objectMapper.readTree(content)
            
            BusinessMetricsResponse(
                todayRevenue = PredictedMetric(
                    value = jsonNode.get("todayRevenue")?.get("value")?.asDouble() ?: 0.0,
                    change = jsonNode.get("todayRevenue")?.get("change")?.asDouble() ?: 0.0,
                    trend = jsonNode.get("todayRevenue")?.get("trend")?.asText() ?: "stable",
                    confidence = jsonNode.get("todayRevenue")?.get("confidence")?.asDouble() ?: 0.5
                ),
                todayVisitors = PredictedMetric(
                    value = jsonNode.get("todayVisitors")?.get("value")?.asDouble() ?: 0.0,
                    change = jsonNode.get("todayVisitors")?.get("change")?.asDouble() ?: 0.0,
                    trend = jsonNode.get("todayVisitors")?.get("trend")?.asText() ?: "stable",
                    confidence = jsonNode.get("todayVisitors")?.get("confidence")?.asDouble() ?: 0.5
                ),
                tomorrowRevenue = PredictedMetric(
                    value = jsonNode.get("tomorrowRevenue")?.get("value")?.asDouble() ?: 0.0,
                    change = jsonNode.get("tomorrowRevenue")?.get("change")?.asDouble() ?: 0.0,
                    trend = jsonNode.get("tomorrowRevenue")?.get("trend")?.asText() ?: "stable",
                    confidence = jsonNode.get("tomorrowRevenue")?.get("confidence")?.asDouble() ?: 0.5
                ),
                tomorrowVisitors = PredictedMetric(
                    value = jsonNode.get("tomorrowVisitors")?.get("value")?.asDouble() ?: 0.0,
                    change = jsonNode.get("tomorrowVisitors")?.get("change")?.asDouble() ?: 0.0,
                    trend = jsonNode.get("tomorrowVisitors")?.get("trend")?.asText() ?: "stable",
                    confidence = jsonNode.get("tomorrowVisitors")?.get("confidence")?.asDouble() ?: 0.5
                ),
                recommendations = jsonNode.get("recommendations")?.map { it.asText() } ?: emptyList(),
                generatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        } catch (e: Exception) {
            log.warn("메트릭 응답 파싱 실패: $content", e)
            null
        }
    }

    private fun generateDefaultWeatherInsight(
        forecasts: List<ShortTermForecast>, 
        businessInfo: BusinessInfo?
    ): String {
        if (forecasts.isEmpty()) return "날씨 데이터를 불러올 수 없습니다."
        
        val today = forecasts.firstOrNull()
        val maxRain = today?.hourlyShortTermForecasts
            ?.mapNotNull { it.precipitationProbability }
            ?.maxOrNull() ?: 0
        
        val maxTemp = today?.dailyMaximumTemperature ?: 0.0
        val minTemp = today?.dailyMinimumTemperature ?: 0.0
        
        return when {
            maxRain > 60 -> "오늘 비 예보(${maxRain}%)로 실내 활동 증가 예상. 배달/포장 서비스를 강화하세요."
            maxTemp > 30 -> "무더운 날씨로 냉방 강화 필요. 시원한 메뉴나 서비스를 준비하세요."
            minTemp < 5 -> "추운 날씨로 따뜻한 서비스 제공 필요. 난방을 미리 가동하세요."
            else -> "온화한 날씨로 평균적인 방문객 예상. 일반적인 운영을 준비하세요."
        }
    }

    private fun generateDefaultMetrics(request: BusinessMetricsRequest): BusinessMetricsResponse {
        val baseRevenue = request.historicalData?.averageRevenue ?: 1000000.0
        val baseVisitors = request.historicalData?.averageVisitors ?: 50
        
        // 날씨에 따른 간단한 변동 계산
        val weatherImpact = request.weatherData?.firstOrNull()?.let { forecast ->
            val maxRain = forecast.hourlyShortTermForecasts
                .mapNotNull { it.precipitationProbability }
                .maxOrNull() ?: 0
            
            when {
                maxRain > 60 -> -0.2  // 비오면 20% 감소
                maxRain > 30 -> -0.1  // 비 조금 오면 10% 감소
                else -> 0.0
            }
        } ?: 0.0

        return BusinessMetricsResponse(
            todayRevenue = PredictedMetric(
                value = baseRevenue * (1 + weatherImpact),
                change = weatherImpact * 100,
                trend = if (weatherImpact < 0) "down" else if (weatherImpact > 0) "up" else "stable",
                confidence = 0.6
            ),
            todayVisitors = PredictedMetric(
                value = (baseVisitors * (1 + weatherImpact)).toDouble(),
                change = weatherImpact * 100,
                trend = if (weatherImpact < 0) "down" else if (weatherImpact > 0) "up" else "stable",
                confidence = 0.6
            ),
            tomorrowRevenue = PredictedMetric(
                value = baseRevenue,
                change = 0.0,
                trend = "stable",
                confidence = 0.5
            ),
            tomorrowVisitors = PredictedMetric(
                value = baseVisitors.toDouble(),
                change = 0.0,
                trend = "stable",
                confidence = 0.5
            ),
            recommendations = listOf(
                "날씨 변화에 따른 재고 조정 필요",
                "온라인 프로모션 강화 고려",
                "피크 시간대 직원 배치 최적화"
            ),
            generatedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}