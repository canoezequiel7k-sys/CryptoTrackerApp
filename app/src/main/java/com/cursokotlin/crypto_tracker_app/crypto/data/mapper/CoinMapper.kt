package com.cursokotlin.crypto_tracker_app.crypto.data.mapper

import com.cursokotlin.crypto_tracker_app.crypto.data.remote.dto.CoinGeckoDto
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin

fun CoinGeckoDto.toCryptoCoin(): CryptoCoin {
    return CryptoCoin(
        id = id,
        rank = marketCapRank ?: 0,
        name = name,
        symbol = symbol.uppercase(),
        priceUsd = currentPrice ?: 0.0,
        changePercent24Hr = priceChangePercentage24h ?: 0.0,
        iconUrl = image,
        priceHistory = sparklineIn7d?.price ?: emptyList() //Mapeamos los puntos del grafico
    )
}