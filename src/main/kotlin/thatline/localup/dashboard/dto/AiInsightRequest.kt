package thatline.localup.dashboard.dto

import thatline.localup.etcapi.dto.ShortTermForecast

data class WeatherInsightRequest(
    val weatherData: List<  ShortTermForecast>,
    val businessInfo: BusinessInfo?
)

data class BusinessInfo(
    val name: String?,
    val address: String?,
    val category: String?,
    val description: String?
)

data class WeatherInsightResponse(
    val insight: String,
    val generatedAt: String
)

data class BusinessMetricsRequest(
    val weatherData: List<ShortTermForecast>?,
    val businessInfo: BusinessInfo?,
    val historicalData: HistoricalMetrics?
)

data class HistoricalMetrics(
    val averageRevenue: Double?,
    val averageVisitors: Int?,
    val peakHours: List<Int>?
)

data class BusinessMetricsResponse(
    val todayRevenue: PredictedMetric,
    val todayVisitors: PredictedMetric,
    val tomorrowRevenue: PredictedMetric,
    val tomorrowVisitors: PredictedMetric,
    val recommendations: List<String>,
    val generatedAt: String
)

data class PredictedMetric(
    val value: Double,
    val change: Double,
    val trend: String,
    val confidence: Double
)