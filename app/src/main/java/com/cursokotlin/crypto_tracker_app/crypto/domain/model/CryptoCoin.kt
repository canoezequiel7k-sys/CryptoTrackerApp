package com.cursokotlin.crypto_tracker_app.crypto.domain.model

/** MODELO de Dominio Puro que representa una criptomoneda en la aplicacion. */

data class CryptoCoin(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val iconUrl: String,
    val priceHistory: List<Double> = emptyList(), //historial de precios de 7 dias
    val isFavorite: Boolean = false
)