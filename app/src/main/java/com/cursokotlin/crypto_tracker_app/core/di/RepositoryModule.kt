package com.cursokotlin.crypto_tracker_app.core.di

import com.cursokotlin.crypto_tracker_app.crypto.data.repository.CoinRepositoryImpl
import com.cursokotlin.crypto_tracker_app.crypto.domain.repository.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        impl: CoinRepositoryImpl
    ): CoinRepository
}