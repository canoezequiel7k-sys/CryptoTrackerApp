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
    @SerializedName("current_price") val currentPrice: Double?, // El signo ? significa que el valor es opcional/nulable.
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("market_cap") val marketCap: Double?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("sparkline_in_7d") val sparklineIn7d: SparklineDto? = null //Si por alguna razón la API no manda ese campo en una moneda nueva, Gson asigna null
)

data class SparklineDto(
    //Mapea el arreglo de precios de los últimos 7 días para dibujar gráficos
    @SerializedName("price") val price: List<Double>? = null
)