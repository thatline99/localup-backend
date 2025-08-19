package thatline.localup.chatgpt.restclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import thatline.localup.chatgpt.dto.ChatGptRequest
import thatline.localup.chatgpt.dto.ChatGptResponse
import thatline.localup.chatgpt.exception.ChatGptException
import java.util.concurrent.TimeUnit

@Component
class ChatGptRestClient(
    private val objectMapper: ObjectMapper,
    @Value("\${openai.api-key}")
    private val apiKey: String,
    @Value("\${openai.api-url:https://api.openai.com/v1/chat/completions}")
    private val apiUrl: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    
    fun chat(request: ChatGptRequest): ChatGptResponse {
        try {
            val requestBodyJson = objectMapper.writeValueAsString(request)
            val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
            
            val httpRequest = Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build()
            
            client.newCall(httpRequest).execute().use { response ->
                val responseBody = response.body?.string()
                
                if (!response.isSuccessful) {
                    log.error("ChatGPT API 호출 실패: ${response.code} - ${response.message}")
                    log.error("응답 본문: $responseBody")
                    throw ChatGptException("ChatGPT API 호출 실패: ${response.message} - $responseBody")
                }
                
                if (responseBody.isNullOrBlank()) {
                    throw ChatGptException("응답 본문이 비어있습니다")
                }
                
                return objectMapper.readValue(responseBody, ChatGptResponse::class.java)
            }
        } catch (e: Exception) {
            log.error("ChatGPT API 호출 중 오류 발생", e)
            throw ChatGptException("ChatGPT API 호출 중 오류 발생: ${e.message}", e)
        }
    }
}