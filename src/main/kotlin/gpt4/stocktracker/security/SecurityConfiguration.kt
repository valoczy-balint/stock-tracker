package gpt4.stocktracker.security

import gpt4.stocktracker.user.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val userService: UserService,
    val oAuth2UserService: OAuth2UserService
) {
        @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/login", "/oauth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .and().successHandler(::handleOAuth2LoginSuccess)
            }
            .logout {
                it.permitAll()
            }
        return http.build()
    }

    private fun handleOAuth2LoginSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauthUser = authentication.principal as DefaultOAuth2User
        val email = oauthUser.attributes["email"] as String
        userService.processOAuthPostLogin(email)
        response.sendRedirect("/")
    }
}