package thatline.localup.chatgpt.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import thatline.localup.chatgpt.dto.ChatSessionRequest
import thatline.localup.chatgpt.dto.ChatSessionResponse
import thatline.localup.chatgpt.entity.ChatSession
import thatline.localup.chatgpt.service.ChatGptService
import thatline.localup.common.response.BaseResponse

@RestController
@RequestMapping("/api/chatgpt")
class ChatGptController(
    private val chatGptService: ChatGptService
) {
    
    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatSessionRequest): ResponseEntity<BaseResponse<ChatSessionResponse>> {
        val response = chatGptService.chat(
            sessionId = request.sessionId,
            userId = request.userId,
            userMessage = request.message
        )
        
        return ResponseEntity.ok(BaseResponse.success(response))
    }
    
    @GetMapping("/sessions/{sessionId}")
    fun getSession(@PathVariable sessionId: String): ResponseEntity<BaseResponse<ChatSession?>> {
        val session = chatGptService.getSession(sessionId)
        
        return if (session != null) {
            ResponseEntity.ok(BaseResponse.success(session))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/users/{userId}/sessions")
    fun getUserSessions(@PathVariable userId: String): ResponseEntity<BaseResponse<List<ChatSession>>> {
        val sessions = chatGptService.getUserSessions(userId)
        
        return ResponseEntity.ok(BaseResponse.success(sessions))
    }
}