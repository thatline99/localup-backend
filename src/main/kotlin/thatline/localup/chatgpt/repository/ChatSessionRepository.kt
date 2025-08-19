package thatline.localup.chatgpt.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import thatline.localup.chatgpt.entity.ChatSession

@Repository
interface ChatSessionRepository : MongoRepository<ChatSession, String> {
    fun findByUserId(userId: String): List<ChatSession>
}