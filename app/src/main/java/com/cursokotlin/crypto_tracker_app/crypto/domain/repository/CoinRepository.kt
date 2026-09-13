package com.cursokotlin.crypto_tracker_app.crypto.domain.repository

import com.cursokotlin.crypto_tracker_app.core.util.NetworkError
import com.cursokotlin.crypto_tracker_app.core.util.Result
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import kotlinx.coroutines.flow.Flow

/**   Interfaz/Contrato del Repositorio.   */
//Datos que necesita mediante interfaz
interface CoinRepository {
    //La llamada intentara traer lista de CryptoCoin, devolvera Success con la lista o Error con la causa
    suspend fun getCoins(): Result<List<CryptoCoin>, NetworkError>

    //devuelve una suscripcion. cada vez que insertemos o borremos una moneda favorita en room local, la base de datos emitira automaticamente la lista nueva
    fun getFavoriteCoins(): Flow<List<CryptoCoin>>

    //Alternar a favorito
    suspend fun toggleFavorite(coinId: String, isFavorite: Boolean)
}