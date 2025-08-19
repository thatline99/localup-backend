package thatline.localup.chatgpt.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import thatline.localup.chatgpt.dto.*
import thatline.localup.chatgpt.service.ChatGptService
import thatline.localup.common.response.BaseResponse

@RestController
@RequestMapping("/api/chatgpt")
class ChatGptController(
    private val chatGptService: ChatGptService
) {
    
    @PostMapping("/chat")
    fun chat(
        @RequestBody request: ChatSessionRequest,
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<BaseResponse<ChatSessionResponse>> {
        val response = chatGptService.chat(
            sessionId = request.sessionId,
            userId = userId,
            userMessage = request.message
        )
        
        return ResponseEntity.ok(BaseResponse.success(response))
    }
    
    @GetMapping("/sessions/{sessionId}")
    fun getSession(@PathVariable sessionId: String): ResponseEntity<BaseResponse<ChatSessionDetailResponse?>> {
        val session = chatGptService.getSessionDetail(sessionId)
        
        return if (session != null) {
            ResponseEntity.ok(BaseResponse.success(session))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/sessions")
    fun getUserSessions(@AuthenticationPrincipal userId: String): ResponseEntity<BaseResponse<List<ChatSessionListResponse>>> {
        val sessions = chatGptService.getUserSessions(userId)
        
        return ResponseEntity.ok(BaseResponse.success(sessions))
    }
    
    @DeleteMapping("/sessions/{sessionId}")
    fun deleteSession(
        @PathVariable sessionId: String,
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<BaseResponse<String>> {
        chatGptService.deleteSession(sessionId, userId)
        
        return ResponseEntity.ok(BaseResponse.success("세션이 삭제되었습니다."))
    }
}