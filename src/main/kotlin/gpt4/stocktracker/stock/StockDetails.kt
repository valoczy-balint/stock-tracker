package gpt4.stocktracker.stock

data class StockDetails(
        val symbol: String,
        val name: String,
        val price: Double,
        val dividendYield: Double?,
        val movingAverage: Double?
) {
    companion object {
        fun create(
        monthlyAdjustedData: StockTimeseriesResponse,
        dailyAdjustedData: StockTimeseriesResponse,
        metaData: StockSearchResponse
        ): StockDetails {
            val symbol = metaData.symbol
            val companyName = metaData.name

            val latestDate = dailyAdjustedData.timeSeries.keys.maxOrNull()
                    ?: throw RuntimeException("TimeSeries keys not found")

            val latestData = dailyAdjustedData.timeSeries[latestDate]
                    ?: throw RuntimeException("TimeSeries latest data not found")

            val price = latestData["5. adjusted close"]?.toDoubleOrNull() ?: 0.0

            // Calculate the annual dividends by summing up the dividends for the past 12 months
            val annualDividends = monthlyAdjustedData.timeSeries.values
                    .take(12)
                    .mapNotNull { it["7. dividend amount"]?.toDoubleOrNull() }
                    .sum()

            val dividendYield = if (price > 0) (annualDividends / price) * 100 else null
            val movingAverage = monthlyAdjustedData.timeSeries.values.map {
                it["5. adjusted close"]?.toDoubleOrNull() ?: 0.0
            }.average()

            return StockDetails(symbol, companyName, price, dividendYield, movingAverage)
        }
    }
}
