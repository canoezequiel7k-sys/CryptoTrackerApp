package com.cursokotlin.crypto_tracker_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cursokotlin.crypto_tracker_app.core.navigation.BottomBarTab
import com.cursokotlin.crypto_tracker_app.core.navigation.Route
import com.cursokotlin.crypto_tracker_app.core.navigation.components.BottomNavigationBar
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_detail.CoinDetailScreen
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.CoinListEvent
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.CoinListScreen
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.CoinListViewModel
import com.cursokotlin.crypto_tracker_app.crypto.presentation.favorites.FavoritesScreen
import com.cursokotlin.crypto_tracker_app.crypto.presentation.profile.ProfileScreen
import com.cursokotlin.crypto_tracker_app.ui.theme.Crypto_tracker_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Crypto_tracker_appTheme {
                // hildviewModel() le pide a hilt que busque la instancia de CoinListViewModel
                val viewModel: CoinListViewModel = hiltViewModel()

                // CollectAsStateWithLifecycle escucha las emisiones de StateFlow
                val state by viewModel.state.collectAsStateWithLifecycle()

                // controlador de navegacion
                val navController = rememberNavController()

                // Leemos la ruta activa para saber cuál pestaña resaltar en la BottomBar
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Determinamos si debemos ocultar la BottomBar cuando estamos en el detalle
                val showBottomBar = currentRoute != Route.CoinDetail::class.qualifiedName

                //  Envolvemos con Scaffold para sostener la BottomBar
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            val activeTab = when (currentRoute) {
                                Route.Favorites::class.qualifiedName -> BottomBarTab.FAVORITES
                                Route.Profile::class.qualifiedName -> BottomBarTab.PROFILE
                                else -> BottomBarTab.HOME
                            }

                            BottomNavigationBar(
                                currentRoute = activeTab.route,
                                onTabSelected = { tab ->
                                    navController.navigate(tab.route) {
                                        popUpTo(Route.CoinList) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding -> // innerPadding respeta la altura de la BottomBar para que las listas no queden tapadas por la barra
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), // Aplicamos el padding aquí
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Controlador del grafo
                        NavHost(
                            navController = navController,
                            startDestination = Route.CoinList
                        ) {
                            // Pantalla 1: Lista de CryptoCoins (Inicio)
                            composable<Route.CoinList> {
                                CoinListScreen(
                                    state = state,
                                    onEvent = { event ->
                                        when (event) {
                                            is CoinListEvent.OnCoinClick -> {
                                                navController.navigate(Route.CoinDetail(coinId = event.coin.id))
                                            }
                                            else -> viewModel.onEvent(event)
                                        }
                                    }
                                )
                            }

                            // Pantalla Mis Monedas (Favoritas)
                            composable<Route.Favorites> {
                                val favoriteCoins = state.coins.filter { it.isFavorite }
                                FavoritesScreen(
                                    favoriteCoins = favoriteCoins,
                                    onCoinClick = { coin ->
                                        navController.navigate(Route.CoinDetail(coinId = coin.id))
                                    }
                                )
                            }

                            // Pantalla Perfil de Usuario
                            composable<Route.Profile> {
                                ProfileScreen()
                            }

                            // Pantalla Detalles de CryptoCoins
                            composable<Route.CoinDetail> { backStackEntry ->
                                val route: Route.CoinDetail = backStackEntry.toRoute()
                                val selectedCoin = state.coins.find { it.id == route.coinId }

                                CoinDetailScreen(
                                    coin = selectedCoin,
                                    onBackClick = {
                                        navController.popBackStack()
                                    },
                                    onFavoriteClick = {
                                        selectedCoin?.let { coin ->
                                            viewModel.onEvent(CoinListEvent.OnToggleFavorite(coin))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}