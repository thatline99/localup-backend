package thatline.localup.chatgpt.restclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import thatline.localup.chatgpt.dto.ChatGptRequest
import thatline.localup.chatgpt.dto.ChatGptResponse
import thatline.localup.chatgpt.dto.Message
import thatline.localup.chatgpt.exception.ChatGptException
import java.util.concurrent.TimeUnit

@Component
class ChatGptRestClient(
    private val objectMapper: ObjectMapper,
    @Value("\${openai.api-key}")
    private val apiKey: String,
    @Value("\${openai.api-url:https://api.openai.com/v1/chat/completions}")
    private val apiUrl: String,
    private val restClient: RestClient,
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

    fun analyzeAndAdviseTourismData(tourismData: String) {
        // TODO-noah: 카페에 한정하지 않고, 사용자 정보를 전달해서 할 필요 있음.
        // NOTE-noah: 관리자 페이지 있다면 거기서 프롬프트를 관리할 수 있도록하면 좋을 것 같음
        val systemPrompt = """
           당신은 카페 운영 컨설턴트입니다. 날씨와 방문자 수 데이터를 보고 카페 운영에 필요한 구체적인 조언만 제공하세요.
           
           답변 규칙:
           - 인사말, 설명, 제목, 구분선 없이 바로 본론으로 시작
           - 불필요한 서론이나 분석 과정 설명 금지
           - 카페 운영자 입장에서 오늘, 내일, 모레 어떻게 준비해야 할지만 말하기
           - 구체적인 실행 방법과 예상 매출 영향 중심으로 작성
           - 리스트 형식(- 또는 1., 2. 등)으로 정리해서 작성
           - 명사형 종결법 사용 (예: ~해야 함, ~필요, ~권장)
        """.trimIndent()

        // NOTE-noah: maxTokens을 50으로 했으나, 너무 작음, 일단 500 설정, 추후 팀과 상의
        val request = ChatGptRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(role = "system", content = systemPrompt),
                Message(role = "user", content = "다음 관광 데이터를 분석해주세요:\n\n$tourismData")
            ),
            temperature = 0.7,
            maxTokens = 500
        )

        val chatGptResponse = restClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .body(request)
            .retrieve()
            .body(ChatGptResponse::class.java)

        if (chatGptResponse != null) {
            log.info(chatGptResponse.choices[0].message.content)
        } else {
            log.info("내용 없음")
        }
    }
}
