package gpt4.stocktracker.stock

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class StockService {
    private val apiKey = "your-api-key"
    private val restTemplate = RestTemplate()
    private val searchUrl = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords={query}&apikey={apiKey}"
    private val stockDataMonthlyAdjustedUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol={symbol}&apikey={apiKey}"
    private val stockDataDailyAdjustedUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol={symbol}&apikey={apiKey}"

    fun searchStocks(query: String): List<StockSearchResponse> {
        val response = restTemplate.getForObject(searchUrl, StockSearchListResponse::class.java, query, apiKey)
        return response?.bestMatches ?: emptyList()
    }

    fun getStockDetails(symbol: String): StockDetails {
        return getStockData(symbol)
    }

    fun getStockData(symbol: String): StockDetails {
        val monthlyAdjustedData = getMonthlyAdjustedStockData(symbol)
        val dailyData = getDailyStockData(symbol)
        val metaData = searchStocks(symbol)[0]

        return StockDetails.create(monthlyAdjustedData!!, dailyData!!, metaData)
    }

    private fun getMonthlyAdjustedStockData(symbol: String) =
            restTemplate.getForObject(stockDataMonthlyAdjustedUrl, StockTimeseriesResponse::class.java, symbol, apiKey)

    private fun getDailyStockData(symbol: String) =
            restTemplate.getForObject(stockDataDailyAdjustedUrl, StockTimeseriesResponse::class.java, symbol, apiKey)
}

data class StockSearchListResponse(
        @JsonProperty("bestMatches")
        val bestMatches: List<StockSearchResponse>
)

data class StockTimeseriesResponse(
        @JsonProperty("Monthly Adjusted Time Series")
        val timeSeries: Map<String, Map<String, String>>,
        @JsonProperty("Meta Data")
        val metaData: Map<String, String>
)

data class StockSearchResponse(
        @JsonProperty("1. symbol")
        val symbol: String,
        @JsonProperty("2. name")
        val name: String
)
