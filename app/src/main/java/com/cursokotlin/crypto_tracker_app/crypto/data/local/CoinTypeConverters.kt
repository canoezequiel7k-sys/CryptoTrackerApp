package com.cursokotlin.crypto_tracker_app.crypto.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Enseña a Room cómo guardar y leer listas de números (List<Double>) en SQLite.
 */
class CoinTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromPriceHistoryList(list: List<Double>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toPriceHistoryList(json: String): List<Double> {
        val type = object : TypeToken<List<Double>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}