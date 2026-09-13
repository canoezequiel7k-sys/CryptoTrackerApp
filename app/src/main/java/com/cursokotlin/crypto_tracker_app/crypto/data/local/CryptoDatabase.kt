package com.cursokotlin.crypto_tracker_app.crypto.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cursokotlin.crypto_tracker_app.crypto.data.local.dao.CoinDao
import com.cursokotlin.crypto_tracker_app.crypto.data.local.entity.CoinEntity


//Base de datos principal del proyecto
@Database(
    entities = [CoinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase(){
    abstract val coinDao : CoinDao
}