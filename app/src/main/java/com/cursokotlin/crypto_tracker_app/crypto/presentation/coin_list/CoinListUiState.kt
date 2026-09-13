package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list

import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin

//Representa el estado inmutable de la pantalla de lista de cryptomonedas
data class CoinListUiState(
    //false: Si es true, mostraremos un spinner de carga (CircularProgressIndicator)
    val isLoading: Boolean = false,
    //La lista de criptomonedas a renderizar
    val coins: List<CryptoCoin> = emptyList(),
    //Si no hay internet o la API falla, este mensaje se llena para mostrar un Banner de Error con botón de reintentar
    val errorMessage: String? = null
)
