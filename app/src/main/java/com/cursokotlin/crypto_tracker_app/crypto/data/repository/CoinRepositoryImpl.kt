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
                //Leemos los IDs de las favoritas actuales guardadas en Room
                val favoriteCoinIds = dao.getFavoriteCoins().first().map { it.id }.toSet()

                //Mapeamos la red preservando 'isfavorite = true' si el id ya era favorito
                val coinEntities = networkResult.data.map { dto ->
                    val coin = dto.toCryptoCoin()
                    coin.toCoinEntity(isFavorite = favoriteCoinIds.contains(coin.id))
                }

                //Guardamos en room sin perder los favoritos
                dao.upsertCoin(coinEntities)

                //retornamos las monedas con su estado de favorito real
                Result.Success(coinEntities.map { it.toCryptoCoin() })
            }
            is Result.Error -> {
                //Estrategia OFFLINE FIRST si falla la red, buscamos cache guardada en Room
                val localCoins = dao.getAllCoins().first()

                if (localCoins.isNotEmpty()){
                    //Si tenemos monedas guardadas localmente las retornamos como exito
                    Result.Success(localCoins.map { it.toCryptoCoin() })
                }else{
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