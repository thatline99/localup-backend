package thatline.localup.chatgpt.dto

import java.time.LocalDateTime

data class ChatSessionDetailResponse(
    val id: String,                            // 세션 ID
    val title: String?,                        // 세션 제목
    val userId: String?,                       // 사용자 ID
    val messages: List<ChatMessageResponse>,   // 메시지 목록
    val totalTokens: Int,                      // 총 토큰 사용량
    val createdAt: LocalDateTime,              // 생성 시간
    val updatedAt: LocalDateTime               // 마지막 수정 시간
)

data class ChatMessageResponse(
    val role: String,                          // 메시지 역할 (user, assistant)
    val content: String,                       // 메시지 내용
    val timestamp: LocalDateTime               // 메시지 시간
)