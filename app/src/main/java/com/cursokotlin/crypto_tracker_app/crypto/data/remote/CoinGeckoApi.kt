package com.cursokotlin.crypto_tracker_app.crypto.data.remote

import com.cursokotlin.crypto_tracker_app.crypto.data.remote.dto.CoinGeckoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("api/v3/coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true
    ): Response<List<CoinGeckoDto>>

    companion object {
        const val BASE_URL = "https://api.coingecko.com/"
    }
}