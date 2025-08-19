package thatline.localup.chatgpt.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_sessions")
data class ChatSession(
    @Id
    val id: String? = null,                                    // MongoDB 자동 생성 ID
    val userId: String?,                                       // 사용자 ID (로그인 사용자인 경우)
    val title: String? = null,                                 // 대화 세션 제목 (자동 생성 또는 사용자 지정)
    val messages: MutableList<ChatMessage> = mutableListOf(),  // 대화 메시지 목록
    val createdAt: LocalDateTime = LocalDateTime.now(),        // 세션 생성 시간
    val updatedAt: LocalDateTime = LocalDateTime.now(),        // 세션 최종 수정 시간
    val totalTokens: Int = 0                                   // 총 사용된 토큰 수
)

data class ChatMessage(
    val role: String,                                          // 메시지 역할 (user, assistant, system)
    val content: String,                                       // 메시지 내용
    val tokens: Int = 0,                                        // 메시지에 사용된 토큰 수
    val timestamp: LocalDateTime = LocalDateTime.now()         // 메시지 생성 시간
)