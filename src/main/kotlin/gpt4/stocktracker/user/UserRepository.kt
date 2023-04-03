package gpt4.stocktracker.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
//    @Query("SELECT u FROM User u WHERE u.username = :username")
    fun findByUsername(@Param("username") username: String) : User?
}