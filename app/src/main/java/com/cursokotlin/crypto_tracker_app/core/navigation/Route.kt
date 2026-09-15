package com.cursokotlin.crypto_tracker_app.core.navigation

import kotlinx.serialization.Serializable


//Sellado de rutas de Navegacion TipoSeguras para Compose
sealed interface Route {
    //Como la pantalla de lista no requiere ningún parámetro para abrirse, la definimos como un data object singleton serializable.
    @Serializable
    data object CoinList: Route
    //La pantalla de detalle necesita saber qué criptomoneda mostrar
    @Serializable
    data class CoinDetail(val coinId: String): Route
    @Serializable
    data object Favorites: Route
    @Serializable
    data object Profile: Route
}