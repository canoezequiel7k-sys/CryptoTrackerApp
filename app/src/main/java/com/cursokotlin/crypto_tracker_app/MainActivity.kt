package com.cursokotlin.crypto_tracker_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cursokotlin.crypto_tracker_app.core.navigation.Route
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_detail.CoinDetailScreen
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.CoinListScreen
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.CoinListViewModel
import com.cursokotlin.crypto_tracker_app.ui.theme.Crypto_tracker_appTheme
import com.cursokotlin.crypto_tracker_app.ui.theme.greenPositive
import com.cursokotlin.crypto_tracker_app.ui.theme.redNegative
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Crypto_tracker_appTheme {
                //hildviewModel() le pide a gilt que busque la instancia de cointListViewModel
                val viewModel: CoinListViewModel = hiltViewModel()

                //CollectAsStateWithLifecycle escucha las emisiones de StateFlow respetando el ciclo de vida de la Activity
                val state by viewModel.state.collectAsStateWithLifecycle()

                //controlado de navegacion
                val navController = rememberNavController()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Controlador del grafo
                    NavHost(
                        navController = navController,
                        startDestination = Route.CoinList
                    ){
                        //Pantalla 1 Lista de cryptoCoin
                        composable<Route.CoinList>{
                            CoinListScreen(
                                state = state,
                                onCoinClick = { coin ->
                                    //navegacion tipo segura pasando el id
                                    navController.navigate(Route.CoinDetail(coinId = coin.id))
                                },
                                onRetryClick = {
                                    viewModel.loadCoins()
                                }
                            )
                        }

                        //Pantalla 2: Detalles de crypocoins
                        composable<Route.CoinDetail> { backStackEntry ->
                            //Desempaquetamos los argumentos de la ruta tipoSegura
                            val route: Route.CoinDetail = backStackEntry.toRoute()

                            //Buscamos la moneda seleccionada dentro de la lista cargada en el estado
                            val selectedCoin = state.coins.find { it.id == route.coinId }

                            CoinDetailScreen(
                                coin = selectedCoin,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hola, $name!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "+5.42% \uD83D\uDCC8",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.greenPositive
            )
            Text(
                text = "-2.10% \uD83D\uDCC9",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.redNegative
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Crypto_tracker_appTheme {
        Greeting("CryptoTracker")
    }
}