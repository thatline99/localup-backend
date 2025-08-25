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
import java.time.LocalDateTime

@Service
class ChatGptService(
    private val chatGptRestClient: ChatGptRestClient,
    private val chatSessionRepository: ChatSessionRepository,
    private val tokenCounter: TokenCounter,
    private val objectMapper: ObjectMapper,
    @Value("\${openai.max-context-tokens:3000}")
    private val maxContextTokens: Int,
    @Value("\${openai.model:gpt-4o-mini}")
    private val defaultModel: String,
    @Value("\${openai.temperature:0.7}")
    private val defaultTemperature: Double,
    @Value("\${openai.max-tokens:1000}")
    private val defaultMaxTokens: Int,
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
    """.trimIndent()

    fun chat(sessionId: String?, userId: String?, userMessage: String): ChatSessionResponse {
        val session = getOrCreateSession(sessionId, userId)

        addUserMessage(session, userMessage)

        return try {
            val response = sendChatRequest(session.messages)
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

    private fun sendChatRequest(messages: List<ChatMessage>): ChatGptResponse {
        val messagesToSend = optimizeMessages(messages)
        val apiMessages = buildApiMessages(messagesToSend)

        val request = ChatGptRequest(
            model = defaultModel,
            messages = apiMessages,
            temperature = defaultTemperature,
            maxTokens = defaultMaxTokens
        )

        return chatGptRestClient.chat(request)
    }

    private fun buildApiMessages(messages: List<ChatMessage>): List<Message> {
        val apiMessages = mutableListOf(
            Message(role = "system", content = systemPrompt)
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

    private fun optimizeMessages(messages: List<ChatMessage>): List<ChatMessage> {
        if (messages.isEmpty()) return emptyList()

        val optimizedMessages = mutableListOf<ChatMessage>()
        var currentTokens = tokenCounter.countTokens(systemPrompt)

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

    // 캐싱, 하루 단위
    fun getInsight(data: String): String {
        return chatGptRestClient.getInsight(data)
    }
}
