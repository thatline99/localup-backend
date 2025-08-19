package thatline.localup.chatgpt.dto

import java.time.LocalDateTime

data class ChatSessionListResponse(
    val id: String,                            // 세션 ID
    val title: String,                         // 세션 제목
    val userId: String?,                       // 사용자 ID
    val totalMessages: Int,                    // 총 메시지 수
    val totalTokens: Int,                      // 총 토큰 사용량
    val createdAt: LocalDateTime,              // 생성 시간
    val updatedAt: LocalDateTime               // 마지막 수정 시간
)