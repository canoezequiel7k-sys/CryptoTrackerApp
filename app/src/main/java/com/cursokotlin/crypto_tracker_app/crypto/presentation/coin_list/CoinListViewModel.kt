package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.crypto_tracker_app.core.util.Result
import com.cursokotlin.crypto_tracker_app.crypto.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//ViewModel que gestiona la lógica de presentación para la lista de criptomonedas. Anotado con @HiltViewModel para que Hilt sepa inyectar sus dependencias.
@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: CoinRepository
): ViewModel() {
    init {
        //En cuanto se instancia el viewmodel, lanzamos la prueba de lectura
        loadCoinTest()
    }

    private fun loadCoinTest() {
        //ViewmodelScope asegura que la coroutine se cancele automaticamente si el viewmodel se destruye
        viewModelScope.launch {
            Log.d("CryptoTrackerTest", "🚀 Iniciando petición a CoinCap API...")

            when(val result = repository.getCoins()){
                is Result.Success -> {
                    val coins = result.data
                    Log.d("CryptoTrackerTest", "✅ ¡ÉXITO! Se cargaron ${coins.size} criptomonedas desde la API.")

                    //Imprimimos en el logcat las primeras 5 para validar datos reales
                    coins.take(5).forEach { coin ->
                        Log.d(
                            "CryptoTrackerTest",
                            "🪙 [#${coin.rank}] ${coin.name} (${coin.symbol}) -> Precio: $${coin.priceUsd} USD | 24h: ${coin.changePercent24Hr}% | Logo: ${coin.iconUrl}"
                        )
                    }
                }
                is Result.Error -> {
                    Log.e("CryptoTrackerTest", "❌ ERROR al consultar la API: ${result.error}")
                }
            }
        }
    }
}