package gpt4.stocktracker

import gpt4.stocktracker.stock.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByUserId(userId: Long): List<Stock>
}