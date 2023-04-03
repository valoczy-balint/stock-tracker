package gpt4.stocktracker.security

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service

@Service
class OAuth2UserService : DefaultOAuth2UserService() {
 
    @Override
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest);
        return OAuth2User(user);
    }
 
}