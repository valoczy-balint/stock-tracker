package gpt4.stocktracker.stock

import gpt4.stocktracker.StockRepository
import gpt4.stocktracker.user.User
import gpt4.stocktracker.user.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api")
class StockController(
    val stockService: StockService,
    val userRepository: UserRepository,
    val stockRepository: StockRepository
) {
    @GetMapping("/search")
    fun searchStocks(principal: Principal, @RequestParam query: String) =
        userRepository.findByUsername(principal.name)?.let { user ->
            ResponseEntity.ok(
                stockService.searchStocks(query).map {
                    Stock(
                        symbol = it.symbol,
                        name = it.name,
                        user = user
                    )
                })
        } ?: ResponseEntity.notFound().build<User>()


    @GetMapping("/stocks/{symbol}")
    fun getStockDetails(@PathVariable symbol: String): ResponseEntity<StockDetails> {
        return ResponseEntity.ok(stockService.getStockDetails(symbol))
    }

    @GetMapping("/favorites")
    fun getFavoriteStocks(principal: Principal): ResponseEntity<List<Stock>> {
        val user = userRepository.findByUsername(principal.name)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(stockRepository.findByUserId(user.id))
    }

    @PostMapping("/favorites/{symbol}")
    fun addStockToFavorites(principal: Principal, @PathVariable symbol: String): ResponseEntity<Stock> {
        val user = userRepository.findByUsername(principal.name)
            ?: return ResponseEntity.notFound().build()
        val stockDetails = stockService.getStockDetails(symbol)

        val stock = Stock(
            symbol = stockDetails.symbol,
            name = stockDetails.name,
            user = user
        )

        user.favoriteStocks.add(stock)
        userRepository.save(user)

        return ResponseEntity.ok(stock)
    }
}