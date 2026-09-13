package com.cursokotlin.crypto_tracker_app.crypto.data.mapper

import com.cursokotlin.crypto_tracker_app.crypto.data.local.entity.CoinEntity
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin


fun CoinEntity.toCryptoCoin(): CryptoCoin{
    return CryptoCoin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        iconUrl = iconUrl
    )
}

fun CryptoCoin.toCoinEntity(isFavorite: Boolean = false): CoinEntity{
    return CoinEntity(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        iconUrl = iconUrl,
        isFavorite = isFavorite
    )
}