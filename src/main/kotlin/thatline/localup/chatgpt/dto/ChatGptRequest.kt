package thatline.localup.chatgpt.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ChatGptRequest(
    val model: String = "gpt-4o-mini",         // 사용할 OpenAI 모델
    val messages: List<Message>,               // 대화 메시지 목록
    val temperature: Double = 0.7,             // 응답 창의성 수준 (0.0~2.0)
    @JsonProperty("max_tokens") 
    val maxTokens: Int? = null                 // 최대 응답 토큰 수
)

data class Message(
    val role: String,                          // 메시지 역할 (system, user, assistant)
    val content: String                        // 메시지 내용
)

data class ChatRequest(
    val message: String                        // 사용자 입력 메시지
)