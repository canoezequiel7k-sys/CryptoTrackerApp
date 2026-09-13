package com.cursokotlin.crypto_tracker_app.crypto.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cursokotlin.crypto_tracker_app.crypto.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow


//Objeto de Acceso a datos (DAO) para realizar consultas SQL en la tabla "Coins"
@Dao
interface CoinDao {
    @Query("SELECT * FROM coins ORDER BY rank ASC")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE isFavorite = 1 ORDER BY rank ASC")
    fun getFavoriteCoins(): Flow<List<CoinEntity>>

    @Upsert
    suspend fun upsertCoin(coins: List<CoinEntity>)

    @Query("UPDATE coins SET isFavorite = :isFavorite WHERE id = :coinId")
    suspend fun toggleFavorite(coinId: String, isFavorite: Boolean)

    @Query("DELETE FROM coins")
    suspend fun clearAllCoins()
}