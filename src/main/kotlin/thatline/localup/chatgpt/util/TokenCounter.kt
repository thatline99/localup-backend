package thatline.localup.chatgpt.util

import org.springframework.stereotype.Component
import kotlin.math.ceil

@Component
class TokenCounter {
    
    fun countTokens(text: String): Int {
        val words = text.split(Regex("\\s+"))
        val englishWords = words.count { it.matches(Regex("[a-zA-Z]+")) }
        val koreanChars = text.count { it in '가'..'힣' }
        
        return ceil(englishWords * 1.3 + koreanChars * 0.5).toInt()
    }
    
    fun countMessageTokens(messages: List<Pair<String, String>>): Int {
        return messages.sumOf { (role, content) ->
            countTokens(role) + countTokens(content) + 4
        }
    }
}