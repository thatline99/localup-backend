package thatline.localup.chatgpt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatGptResponse(
    val id: String,                            // 응답 ID
    @JsonProperty("object")
    val objectType: String,                    // 객체 타입 (chat.completion)
    val created: Long,                         // 생성 타임스탬프
    val model: String,                         // 사용된 모델 이름
    val choices: List<Choice>,                 // 응답 선택지 목록
    val usage: Usage                           // 토큰 사용량 정보
)

data class Choice(
    val index: Int,                            // 선택지 인덱스
    val message: ResponseMessage,              // 응답 메시지
    @JsonProperty("finish_reason")
    val finishReason: String?                  // 종료 이유 (stop, length, etc.)
)

data class ResponseMessage(
    val role: String,                          // 메시지 역할 (assistant)
    val content: String?,                      // 메시지 내용
    val refusal: String? = null,               // 거부 메시지 (안전 정책 위반 시)
    val annotations: List<Any>? = null         // 추가 주석 정보
)

data class Usage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,                     // 입력 프롬프트 토큰 수
    @JsonProperty("completion_tokens")
    val completionTokens: Int,                 // 응답 토큰 수
    @JsonProperty("total_tokens")
    val totalTokens: Int                       // 총 토큰 수
)

data class ChatResponse(
    val reply: String                          // 챗봇 응답 메시지
)