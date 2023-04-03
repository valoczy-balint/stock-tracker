package gpt4.stocktracker.stock

import gpt4.stocktracker.user.User
import jakarta.persistence.*

@Entity
data class Stock(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val symbol: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User,

        val name: String
)