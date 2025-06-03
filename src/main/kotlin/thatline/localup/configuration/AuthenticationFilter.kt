package thatline.localup.configuration

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import thatline.localup.property.TokenProperty
import thatline.localup.service.AuthService

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
                // TODO: noah, 커스텀 토큰으로 변경
                val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())

                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}
