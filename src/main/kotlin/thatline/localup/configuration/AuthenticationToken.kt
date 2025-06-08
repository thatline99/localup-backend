package thatline.localup.configuration

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticationToken(
    private val userId: String,
    authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any? = null

    override fun getPrincipal(): String = userId

    init {
        super.setAuthenticated(true)
    }
}
