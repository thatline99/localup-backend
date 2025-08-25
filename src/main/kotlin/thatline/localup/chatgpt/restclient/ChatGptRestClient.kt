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

    fun getInsight(data: String): String {
        // NOTE-noah: 관리자 페이지 있다면 거기서 프롬프트를 관리할 수 있도록하면 좋을 것 같음
        // NOTE-noah: GPT 프롬프트 수정 필요 (협업 필요)
        val systemPrompt = """
            제공된 데이터만을 사용하여 자영업자에게 필요한 구체적 조치사항을 작성하세요.
        
            ## 엄격한 제약사항
            - 데이터에 명시되지 않은 내용은 절대 언급 금지
            - 추측, 가정, 일반적 조언 완전 금지  
            - 주어진 사업 정보에 포함된 내용만 활용
            - 주어진 기상 데이터의 정확한 수치만 사용
            - 주어진 방문객 통계의 실제 수치만 활용
        
            ## 작성 방법
            1. 먼저 사업 정보를 정확히 파악
            2. 그 업종에서 실제로 일어날 수 있는 상황만 고려
            3. 기상 데이터의 정확한 시간과 수치 인용
            4. 작년 방문객 수치를 정확히 계산하여 비교
        
            ## 출력 형식
            - 제목, 인사말 없이 바로 조치사항 시작
            - 각 문장: [정확한 데이터] → [구체적 행동] (이유)
            - 데이터에 근거한 내용만 포함
            - 시설이나 장비 언급 시 반드시 해당 업종에서 실제 사용하는 것만
        
            ## 데이터 검증
            사업 정보에서:
            - 정확한 업종은?
            - 언급된 시설이나 특징은?
            - 위치나 규모 정보는?
        
            기상 데이터에서:
            - 정확한 강수확률과 시간대는?
            - 정확한 온도와 습도는?
            - 특별히 주의할 기상 조건은?
        
            방문객 데이터에서:
            - 작년 정확한 방문객 수는?
            - 현지인/외지인/외국인 정확한 비율은?
            - 증감 패턴은?
        
            ## 금지사항
            - 외부 테이블 (언급 없음)
            - 카페 장비 (카페가 아닐 수 있음)  
            - 숙박 시설 (숙박업이 아닐 수 있음)
            - 구체적 언급 없는 모든 시설/장비
            - 데이터 범위를 벗어난 모든 추측
        
            오직 제공된 데이터에서 확실히 확인할 수 있는 내용만 사용하여 해당 업종의 실제 운영에 도움이 되는 조치사항을 작성하세요.
        """.trimIndent()

        // NOTE-noah: maxTokens을 50으로 했으나, 너무 작음, 일단 500 설정, 추후 팀과 상의
        // TODO-noah: property 분리
        val request = ChatGptRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(role = "system", content = systemPrompt),
                Message(role = "user", content = data)
            ),
            temperature = 0.7,
            maxTokens = 500
        )

        // TODO-noah: 응답 실패, 처리 필요

        val chatGptResponse = restClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .body(request)
            .retrieve()
            .body(ChatGptResponse::class.java)

        return chatGptResponse?.choices?.get(0)?.message?.content ?: ""
    }
}
