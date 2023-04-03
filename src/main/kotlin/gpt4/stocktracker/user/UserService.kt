package gpt4.stocktracker.user

import org.springframework.stereotype.Service;

@Service
class UserService(
    val repo: UserRepository
) {
    fun processOAuthPostLogin(username: String) {
        repo.findByUsername(username) ?: run {
            User(
                username = username
            ).let {
                repo.save(it)
            }
        }
    }
}