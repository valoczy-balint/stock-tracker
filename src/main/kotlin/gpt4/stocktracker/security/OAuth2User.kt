package gpt4.stocktracker.security

import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuth2User(private val oauth2User: OAuth2User) : OAuth2User by oauth2User {
    fun getEmail(): String? {
        return oauth2User.getAttribute<String>("email")
    }
}

// class OAuth2User implements OAuth2User {
//
//    private OAuth2User oauth2User;
//
//    public CustomOAuth2User(OAuth2User oauth2User) {
//        this.oauth2User = oauth2User;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return oauth2User.getAttributes();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return oauth2User.getAuthorities();
//    }
//
//    @Override
//    public String getName() {
//        return oauth2User.getAttribute("name");
//    }
//
//    public String getEmail() {
//        return oauth2User.<String>getAttribute("email");
//    }
//}