package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.crypto_tracker_app.core.util.NetworkError
import com.cursokotlin.crypto_tracker_app.core.util.Result
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


//ViewModel que gestiona la lógica de presentación para la lista de criptomonedas. Anotado con @HiltViewModel para que Hilt sepa inyectar sus dependencias.
@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: CoinRepository
): ViewModel() {

    //Estato privadp mutable (solo el viewModel puede modificarlo)
    private val _state = MutableStateFlow(CoinListUiState())

    //estado publico inmutable (la UI solo puede leerlo)
    val state: StateFlow<CoinListUiState> = _state.asStateFlow()

    //Copia de respaldo de todas las monedas originales sin filtrar
    private var allCoins = emptyList<CryptoCoin>()


    init {
        loadCoins()
    }

    //Funcion centralizada para procesas todos los eventos emitidos por la UI
    fun onEvent(event: CoinListEvent){
        when(event){
            is CoinListEvent.OnSearchQueryChange -> {
                filterCoins(event.query)
            }
            is CoinListEvent.OnToggleFavorite -> {
                toggleFavorite(event.coin)
            }
            is CoinListEvent.OnRetryClick -> {
                loadCoins()
            }
            is CoinListEvent.OnCoinClick -> {
                //Navegacion se maneja desde el NavHost en MainActivity
            }
        }
    }

    private fun toggleFavorite(coin: CryptoCoin) {
        viewModelScope.launch {
            val newFavoriteState = !coin.isFavorite

            //Actualizamos en la base de datos room
            repository.toggleFavorite(coin.id, newFavoriteState)

            //Actualizamos la lista de respaldo en memoria(allCoins)
            allCoins = allCoins.map {
                if (it.id == coin.id) it.copy(isFavorite = newFavoriteState) else it
            }

            //Emitimos el nuevo UiState de la Ui
            _state.update { currentState ->
                val updateCoins = currentState.coins.map {
                    if (it.id == coin.id) it.copy(isFavorite = newFavoriteState) else it
                }
                currentState.copy(coins = updateCoins)
            }

        }
    }

    fun loadCoins() {
        viewModelScope.launch{
            //Emite estado de carga habilitado
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when(val result = repository.getCoins()){
                is Result.Success ->{
                    allCoins = result.data //guardamos la copia de respaldo

                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = result.data
                        )
                    }
                }
                is Result.Error -> {
                    val message = when(result.error){
                        NetworkError.NO_INTERNET -> "Sin conexión a Internet. Verifica tu red."
                        NetworkError.REQUEST_TIMEOUT -> "Tiempo de espera agotado."
                        NetworkError.TOO_MANY_REQUESTS -> "Demasiadas peticiones a CoinGecko."
                        NetworkError.SERVER_ERROR -> "Error interno en el servidor."
                        NetworkError.SERIALIZATION -> "Error al procesar la respuesta."
                        NetworkError.UNKNOWN -> "Error desconocido."
                    }
                    //Es una función atómica segura para subprocesos de MutableStateFlow. Evita condiciones de carrera si dos corrutinas intentan actualizar el estado al mismo tiempo.
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                }
            }
        }
    }



    //Filtrar la lista de monedas por Nombre o Symbol
    private fun filterCoins(query: String){
        _state.update { currentState ->
            val filteredList = if (query.isBlank()){
                allCoins
            } else {
                allCoins.filter { coin ->
                    coin.name.contains(query, ignoreCase = true) ||
                            coin.symbol.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                coins = filteredList
            )
        }
    }



}