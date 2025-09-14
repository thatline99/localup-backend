package thatline.localup.chatgpt.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import thatline.localup.chatgpt.dto.*
import thatline.localup.chatgpt.entity.ChatMessage
import thatline.localup.chatgpt.entity.ChatSession
import thatline.localup.chatgpt.repository.ChatSessionRepository
import thatline.localup.chatgpt.restclient.ChatGptRestClient
import thatline.localup.chatgpt.util.TokenCounter
import thatline.localup.user.service.UserService
import thatline.localup.user.dto.FindBusinessDto
import java.time.LocalDateTime

@Service
class ChatGptService(
    private val chatGptRestClient: ChatGptRestClient,
    private val chatSessionRepository: ChatSessionRepository,
    private val tokenCounter: TokenCounter,
    private val objectMapper: ObjectMapper,
    private val userService: UserService,
    @Value("\${openai.max-context-tokens:3000}")
    private val maxContextTokens: Int,
    @Value("\${openai.model:gpt-4o-mini}")
    private val defaultModel: String,
    @Value("\${openai.temperature:0.7}")
    private val defaultTemperature: Double,
    @Value("\${openai.max-tokens:1000}")
    private val defaultMaxTokens: Int
) {
    companion object {
        private const val TITLE_MAX_LENGTH = 30
        private const val TITLE_PROMPT_MAX_TOKENS = 50
        private const val NEW_SESSION_MESSAGE_COUNT = 2
        private const val DEFAULT_SESSION_TITLE = "새 대화"
    }
    
    private val log = LoggerFactory.getLogger(this::class.java)
    
    private val systemPrompt = """
        당신은 친절하고 도움이 되는 AI 어시스턴트입니다.
        사용자의 질문에 정확하고 유용한 답변을 제공해주세요.
        답변은 명확하고 이해하기 쉽게 작성해주세요.
        한국어로 대화를 진행합니다.

        응답 형식 (매우 중요):
        - 절대로 마크다운(**bold**, ##제목, - 목록 등)을 사용하지 마세요.
        - 각 섹션마다 빈 줄을 추가하여 단락을 명확히 구분하세요.
        - 긴 문장은 적절한 위치에서 줄바꿈하여 읽기 쉽게 만드세요.
        - 번호 목록: 1. 2. 3. 형식 사용
        - 하위 항목: - 또는 · 기호 사용
        - 제목이나 강조가 필요한 부분은 "대괄호 [강조내용]" 형식 사용
    """.trimIndent()

    private fun getBusinessInfo(userId: String): FindBusinessDto? {
        return try {
            userService.findBusiness(userId)
        } catch (e: Exception) {
            log.warn("사업정보 조회 실패: ${e.message}")
            null
        }
    }

    private fun buildDynamicSystemPrompt(userId: String?): String {
        if (userId == null) return systemPrompt

        val business = getBusinessInfo(userId)

        return if (business != null) {
            """
            당신은 친절하고 도움이 되는 AI 어시스턴트입니다.
            사용자의 질문에 정확하고 유용한 답변을 제공해주세요.
            답변은 명확하고 이해하기 쉽게 작성해주세요.
            한국어로 대화를 진행합니다.

            응답 형식 (매우 중요):
            - 절대로 마크다운(**bold**, ##제목, - 목록 등)을 사용하지 마세요.
            - 각 섹션마다 빈 줄을 추가하여 단락을 명확히 구분하세요.
            - 긴 문장은 적절한 위치에서 줄바꿈하여 읽기 쉽게 만드세요.
            - 번호 목록: 1. 2. 3. 형식 사용
            - 하위 항목: - 또는 · 기호 사용
            - 제목이나 강조가 필요한 부분은 "대괄호 [강조내용]" 형식 사용

            특히 현재 대화하는 사용자는 다음과 같은 사업정보를 가진 사업자입니다:
            - 업체명: ${business.name}
            - 업종: ${business.type} (${business.item})
            - 위치: ${business.address}${if (!business.addressDetail.isNullOrBlank()) " ${business.addressDetail}" else ""}
            - 좌석 수: ${business.seatCount}석
            - 평균 객단가: ${String.format("%.0f", business.averageOrderAmount)}원
            - 주요 고객층: ${business.customerSegments.joinToString(", ")}
            ${if (!business.description.isNullOrBlank()) "- 사업체 특징: ${business.description}" else ""}

            이 사업정보를 바탕으로 해당 업종과 사업 규모에 맞는 전문적이고 실용적인 조언을 제공해주세요.
            관광, 날씨, 지역 특성 등을 활용한 비즈니스 인사이트도 함께 제공하면 더욱 좋습니다.
            """.trimIndent()
        } else {
            systemPrompt
        }
    }
    
    fun chat(sessionId: String?, userId: String?, userMessage: String): ChatSessionResponse {
        val session = getOrCreateSession(sessionId, userId)
        
        addUserMessage(session, userMessage)
        
        return try {
            val response = sendChatRequest(session.messages, userId)
            val reply = extractReply(response)
            
            if (reply.isNullOrBlank()) {
                return createErrorResponse(session, "죄송합니다. 응답을 생성할 수 없습니다.")
            }
            
            addAssistantMessage(session, reply)
            
            val sessionTitle = determineSessionTitle(session, userMessage, reply)
            val savedSession = saveSession(session, sessionTitle)
            
            createSuccessResponse(savedSession, reply)
        } catch (e: Exception) {
            log.error("ChatGPT 서비스 처리 중 오류 발생", e)
            createErrorResponse(session, "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
    }
    
    private fun getOrCreateSession(sessionId: String?, userId: String?): ChatSession {
        return if (sessionId != null) {
            chatSessionRepository.findById(sessionId).orElseGet {
                createNewSession(userId)
            }
        } else {
            createNewSession(userId)
        }
    }
    
    private fun addUserMessage(session: ChatSession, userMessage: String) {
        val userMessageTokens = tokenCounter.countTokens(userMessage)
        session.messages.add(
            ChatMessage(
                role = "user",
                content = userMessage,
                tokens = userMessageTokens
            )
        )
    }
    
    private fun sendChatRequest(messages: List<ChatMessage>, userId: String? = null): ChatGptResponse {
        val messagesToSend = optimizeMessages(messages, userId)
        val apiMessages = buildApiMessages(messagesToSend, userId)

        val request = ChatGptRequest(
            model = defaultModel,
            messages = apiMessages,
            temperature = defaultTemperature,
            maxTokens = defaultMaxTokens
        )

        return chatGptRestClient.chat(request)
    }
    
    private fun buildApiMessages(messages: List<ChatMessage>, userId: String? = null): List<Message> {
        val dynamicSystemPrompt = buildDynamicSystemPrompt(userId)
        val apiMessages = mutableListOf(
            Message(role = "system", content = dynamicSystemPrompt)
        )
        apiMessages.addAll(
            messages.map { Message(role = it.role, content = it.content) }
        )
        return apiMessages
    }
    
    private fun extractReply(response: ChatGptResponse): String? {
        return response.choices.firstOrNull()?.message?.content?.also { reply ->
            if (reply.isNullOrBlank()) {
                log.warn("ChatGPT 응답이 비어있습니다")
            }
        }
    }
    
    private fun addAssistantMessage(session: ChatSession, reply: String) {
        val assistantTokens = tokenCounter.countTokens(reply)
        session.messages.add(
            ChatMessage(
                role = "assistant",
                content = reply,
                tokens = assistantTokens
            )
        )
    }
    
    private fun determineSessionTitle(session: ChatSession, userMessage: String, reply: String): String? {
        return if (session.title == null && session.messages.size == NEW_SESSION_MESSAGE_COUNT) {
            generateSessionTitle(userMessage, reply)
        } else {
            session.title
        }
    }
    
    private fun saveSession(session: ChatSession, title: String?): ChatSession {
        val updatedSession = session.copy(
            title = title,
            updatedAt = LocalDateTime.now(),
            totalTokens = session.messages.sumOf { it.tokens }
        )
        
        return chatSessionRepository.save(updatedSession).also {
            log.info("ChatGPT 응답 생성 완료 - 세션 ID: ${it.id}, 총 토큰: ${it.totalTokens}")
        }
    }
    
    private fun createSuccessResponse(session: ChatSession, reply: String): ChatSessionResponse {
        return ChatSessionResponse(
            sessionId = session.id!!,
            reply = reply,
            title = session.title,
            totalMessages = session.messages.size,
            totalTokens = session.totalTokens
        )
    }
    
    private fun createErrorResponse(session: ChatSession, errorMessage: String): ChatSessionResponse {
        return ChatSessionResponse(
            sessionId = session.id ?: "",
            reply = errorMessage,
            title = session.title,
            totalMessages = session.messages.size,
            totalTokens = session.totalTokens
        )
    }
    
    fun createNewSession(userId: String?): ChatSession {
        return ChatSession(userId = userId)
    }
    
    fun getSession(sessionId: String): ChatSession? {
        return chatSessionRepository.findById(sessionId).orElse(null)
    }
    
    fun getUserSessions(userId: String): List<ChatSessionListResponse> {
        return chatSessionRepository.findByUserId(userId).map { session ->
            ChatSessionListResponse(
                id = session.id!!,
                title = session.title ?: DEFAULT_SESSION_TITLE,
                userId = session.userId,
                totalMessages = session.messages.size,
                totalTokens = session.totalTokens,
                createdAt = session.createdAt,
                updatedAt = session.updatedAt
            )
        }
    }
    
    fun getSessionDetail(sessionId: String): ChatSessionDetailResponse? {
        val session = chatSessionRepository.findById(sessionId).orElse(null)
        return session?.let {
            ChatSessionDetailResponse(
                id = it.id!!,
                title = it.title,
                userId = it.userId,
                messages = it.messages.map { message ->
                    ChatMessageResponse(
                        role = message.role,
                        content = message.content,
                        timestamp = message.timestamp
                    )
                },
                totalTokens = it.totalTokens,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }
    
    private fun optimizeMessages(messages: List<ChatMessage>, userId: String? = null): List<ChatMessage> {
        if (messages.isEmpty()) return emptyList()

        val optimizedMessages = mutableListOf<ChatMessage>()
        val dynamicSystemPrompt = buildDynamicSystemPrompt(userId)
        var currentTokens = tokenCounter.countTokens(dynamicSystemPrompt)

        for (message in messages.reversed()) {
            val messageTokens = message.tokens
            if (currentTokens + messageTokens > maxContextTokens) {
                break
            }
            optimizedMessages.add(0, message)
            currentTokens += messageTokens
        }

        if (optimizedMessages.isNotEmpty() && optimizedMessages.first().role != "user") {
            val firstUserIndex = optimizedMessages.indexOfFirst { it.role == "user" }
            if (firstUserIndex > 0) {
                return optimizedMessages.subList(firstUserIndex, optimizedMessages.size)
            }
        }

        return optimizedMessages
    }
    
    private fun generateSessionTitle(userMessage: String, assistantReply: String): String {
        return try {
            val titlePrompt = """
                사용자 메시지: "$userMessage"
                
                위 메시지의 주제를 10자 이내의 짧은 한글 제목으로 만들어주세요.
                JSON 형식으로 응답: {"title": "제목"}
            """.trimIndent()
            
            val request = ChatGptRequest(
                model = defaultModel,
                messages = listOf(
                    Message(role = "system", content = "대화 제목을 생성하는 도우미입니다."),
                    Message(role = "user", content = titlePrompt)
                ),
                temperature = defaultTemperature,
                maxTokens = TITLE_PROMPT_MAX_TOKENS
            )
            
            val response = chatGptRestClient.chat(request)
            val content = response.choices.firstOrNull()?.message?.content?.trim()
            
            if (!content.isNullOrBlank()) {
                try {
                    val jsonNode = objectMapper.readTree(content)
                    val title = jsonNode.get("title")?.asText()
                    
                    if (!title.isNullOrBlank()) {
                        return title.take(TITLE_MAX_LENGTH)
                    }
                } catch (e: Exception) {
                    log.warn("제목 생성 JSON 파싱 실패: $content")
                }
            }
            
            DEFAULT_SESSION_TITLE
        } catch (e: Exception) {
            log.error("세션 제목 생성 중 오류 발생", e)
            DEFAULT_SESSION_TITLE
        }
    }
    
    fun deleteSession(sessionId: String, userId: String) {
        val session = chatSessionRepository.findById(sessionId).orElse(null)
        if (session != null && session.userId == userId) {
            chatSessionRepository.deleteById(sessionId)
        }
    }
}