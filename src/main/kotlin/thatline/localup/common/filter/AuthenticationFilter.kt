package thatline.localup.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import thatline.localup.auth.service.AuthService
import thatline.localup.common.configuration.AuthenticationToken
import thatline.localup.common.property.TokenProperty

@Component
class AuthenticationFilter(
    private val tokenProperty: TokenProperty,
    private val authService: AuthService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cookies = request.cookies

        val accessToken = cookies?.firstOrNull { it.name == tokenProperty.accessToken.name }?.value

        if (!accessToken.isNullOrBlank()) {
            val userId = authService.findUserIdByAccessToken(accessToken)

            if (userId != null) {
                SecurityContextHolder.getContext().authentication = AuthenticationToken(userId)
            }
        }

        filterChain.doFilter(request, response)
    }
}
