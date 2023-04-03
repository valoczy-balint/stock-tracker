package gpt4.stocktracker.user

import gpt4.stocktracker.stock.Stock
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val username: String,

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        val favoriteStocks: MutableList<Stock> = mutableListOf()
)