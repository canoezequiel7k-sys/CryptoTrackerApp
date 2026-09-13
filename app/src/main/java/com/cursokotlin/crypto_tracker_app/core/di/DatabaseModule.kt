package com.cursokotlin.crypto_tracker_app.core.di

import android.app.Application
import androidx.room.Room
import com.cursokotlin.crypto_tracker_app.crypto.data.local.CryptoDatabase
import com.cursokotlin.crypto_tracker_app.crypto.data.local.dao.CoinDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    @Singleton
    fun provideCryptoDatabase(app: Application): CryptoDatabase{
        return Room.databaseBuilder(
            app,
            CryptoDatabase::class.java,
            "crypto.db"   //Nombre del archivo SQLITE en el almacenamiento interno del dispositivo
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(db: CryptoDatabase): CoinDao{
        return db.coinDao
    }


}