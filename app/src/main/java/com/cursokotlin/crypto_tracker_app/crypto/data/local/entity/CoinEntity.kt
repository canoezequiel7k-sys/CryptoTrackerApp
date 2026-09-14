package com.cursokotlin.crypto_tracker_app.crypto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val iconUrl: String,
    val isFavorite: Boolean = false,
    val priceHistory: List<Double> = emptyList()
)
