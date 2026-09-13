package com.cursokotlin.crypto_tracker_app.crypto.data.repository

import com.cursokotlin.crypto_tracker_app.core.data.safeCall
import com.cursokotlin.crypto_tracker_app.core.util.NetworkError
import com.cursokotlin.crypto_tracker_app.core.util.Result
import com.cursokotlin.crypto_tracker_app.core.util.map
import com.cursokotlin.crypto_tracker_app.crypto.data.local.dao.CoinDao
import com.cursokotlin.crypto_tracker_app.crypto.data.mapper.toCoinEntity
import com.cursokotlin.crypto_tracker_app.crypto.data.mapper.toCryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.data.remote.CoinGeckoApi
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val dao: CoinDao //Inyecto el DAO en Room
) : CoinRepository {

    override suspend fun getCoins(): Result<List<CryptoCoin>, NetworkError> {
        val networkResult = safeCall { api.getCoins() }

        return when (networkResult){
            is Result.Success -> {
                val coins = networkResult.data.map { dto -> dto.toCryptoCoin() }

                //guardamos y actualizamos los datos nuevos de la API en la base de datos local
                dao.upsertCoin(coins.map { it.toCoinEntity() })

                Result.Success(coins)
            }
            is Result.Error -> {
                //Estrategia OFFLINE FIRST si falla la red, buscamos cache guardada en Room
                val localCoins = dao.getAllCoins().first()

                if (localCoins.isNotEmpty()){
                    //Si tenemos monedas guardadas localmente las retornamos como exito
                    Result.Success(localCoins.map { it.toCryptoCoin() })
                }else{
                    //Si no hay datos ni en red ni en base de datos local, notificamos el error
                    Result.Error(networkResult.error)
                }
            }
        }
    }

    override fun getFavoriteCoins(): Flow<List<CryptoCoin>> {
        return dao.getFavoriteCoins().map { entities ->
            entities.map { it.toCryptoCoin() }
        }
    }

    override suspend fun toggleFavorite(coinId: String, isFavorite: Boolean) {
        dao.toggleFavorite(coinId, isFavorite)
    }
}