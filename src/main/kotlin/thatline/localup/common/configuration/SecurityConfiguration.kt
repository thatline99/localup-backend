package thatline.localup.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import thatline.localup.common.filter.AuthenticationFilter
import thatline.localup.constant.Environment

@Configuration
class SecurityConfiguration(
    private val authenticationFilter: AuthenticationFilter,
) {
    @Bean
    @Profile(Environment.LOCAL)
    fun localFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource {
                    val corsConfiguration = CorsConfiguration()

                    corsConfiguration.allowedOrigins = listOf("http://localhost:3000")
                    corsConfiguration.addAllowedHeader("*")
                    corsConfiguration.addAllowedMethod("*")
                    corsConfiguration.allowCredentials = true

                    corsConfiguration
                }
            }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/tour-api/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
