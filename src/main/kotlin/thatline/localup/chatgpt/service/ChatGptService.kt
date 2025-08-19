package thatline.localup.chatgpt.service

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
    @Value("\${openai.max-context-tokens:3000}")
    private val maxContextTokens: Int
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    
    private val systemPrompt = """
        당신은 친절하고 도움이 되는 AI 어시스턴트입니다.
        사용자의 질문에 정확하고 유용한 답변을 제공해주세요.
        답변은 명확하고 이해하기 쉽게 작성해주세요.
        한국어로 대화를 진행합니다.
    """.trimIndent()
    
    fun chat(sessionId: String?, userId: String?, userMessage: String): ChatSessionResponse {
        val session = if (sessionId != null) {
            chatSessionRepository.findById(sessionId).orElseGet {
                createNewSession(userId)
            }
        } else {
            createNewSession(userId)
        }
        
        val userMessageTokens = tokenCounter.countTokens(userMessage)
        session.messages.add(
            ChatMessage(
                role = "user",
                content = userMessage,
                tokens = userMessageTokens
            )
        )
        
        val messagesToSend = optimizeMessages(session.messages)
        
        val apiMessages = mutableListOf(
            Message(role = "system", content = systemPrompt)
        )
        apiMessages.addAll(
            messagesToSend.map { Message(role = it.role, content = it.content) }
        )
        
        val request = ChatGptRequest(
            model = "gpt-5-nano",
            messages = apiMessages,
            temperature = 0.7,
            maxTokens = 1000
        )
        
        return try {
            val response = chatGptRestClient.chat(request)
            val reply = response.choices.firstOrNull()?.message?.content 
                ?: "죄송합니다. 응답을 생성할 수 없습니다."
            
            val assistantTokens = tokenCounter.countTokens(reply)
            session.messages.add(
                ChatMessage(
                    role = "assistant",
                    content = reply,
                    tokens = assistantTokens
                )
            )
            
            val updatedSession = session.copy(
                updatedAt = LocalDateTime.now(),
                totalTokens = session.messages.sumOf { it.tokens }
            )
            
            val savedSession = chatSessionRepository.save(updatedSession)
            
            log.info("ChatGPT 응답 생성 완료 - 세션 ID: ${savedSession.id}, 총 토큰: ${savedSession.totalTokens}")
            
            ChatSessionResponse(
                sessionId = savedSession.id!!,
                reply = reply,
                totalMessages = savedSession.messages.size,
                totalTokens = savedSession.totalTokens
            )
        } catch (e: Exception) {
            log.error("ChatGPT 서비스 처리 중 오류 발생", e)
            ChatSessionResponse(
                sessionId = session.id ?: "",
                reply = "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                totalMessages = session.messages.size,
                totalTokens = session.totalTokens
            )
        }
    }
    
    fun createNewSession(userId: String?): ChatSession {
        return ChatSession(userId = userId)
    }
    
    fun getSession(sessionId: String): ChatSession? {
        return chatSessionRepository.findById(sessionId).orElse(null)
    }
    
    fun getUserSessions(userId: String): List<ChatSession> {
        return chatSessionRepository.findByUserId(userId)
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
}