package thatline.localup.chatgpt.dto

data class ChatSessionResponse(
    val sessionId: String,                     // 세션 ID
    val reply: String,                         // AI 응답 메시지
    val title: String? = null,                 // 세션 제목
    val totalMessages: Int,                    // 총 메시지 수
    val totalTokens: Int                       // 총 사용 토큰 수
)

data class ChatSessionRequest(
    val sessionId: String? = null,             // 기존 세션 ID (이어서 대화하기)
    val message: String                        // 사용자 메시지
)