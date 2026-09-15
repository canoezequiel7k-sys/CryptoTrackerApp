package com.cursokotlin.crypto_tracker_app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector


//Representan las 3 pestañas principales de la barra de navegacion inferior
enum class BottomBarTab(
    val title: String,
    val icon: ImageVector,
    val route: Route
) {
    HOME(
        title = "Inicio",
        icon = Icons.Default.Home,
        route = Route.CoinList
    ),
    FAVORITES(
        title = "Mis Monedas",
        icon = Icons.Default.Star,
        route = Route.Favorites
    ),
    PROFILE(
        title = "Perfil",
        icon = Icons.Default.Person,
        route = Route.Profile
    )

}