package com.cursokotlin.crypto_tracker_app.crypto.data.repository

import com.cursokotlin.crypto_tracker_app.core.data.safeCall
import com.cursokotlin.crypto_tracker_app.core.util.NetworkError
import com.cursokotlin.crypto_tracker_app.core.util.Result
import com.cursokotlin.crypto_tracker_app.core.util.map
import com.cursokotlin.crypto_tracker_app.crypto.data.mapper.toCryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.data.remote.CoinGeckoApi
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi
) : CoinRepository {

    override suspend fun getCoins(): Result<List<CryptoCoin>, NetworkError> {
        return safeCall {
            api.getCoins()
        }.map { coinDtos ->
            coinDtos.map { dto ->
                dto.toCryptoCoin()
            }
        }
    }

    override fun getFavoriteCoins(): Flow<List<CryptoCoin>> {
        return flowOf(emptyList())
    }
}