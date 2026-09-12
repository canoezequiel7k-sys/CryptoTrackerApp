package com.cursokotlin.crypto_tracker_app.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para mapear la respuesta de CoinGecko API:
 * https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd
 */
data class CoinGeckoDto(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("current_price") val currentPrice: Double?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("market_cap") val marketCap: Double?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("sparkline_in_7d") val sparklineIn7d: SparklineDto? = null
)

data class SparklineDto(
    @SerializedName("price") val price: List<Double>? = null
)