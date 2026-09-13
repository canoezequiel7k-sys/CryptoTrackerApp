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
                val context = LocalContext.current


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoinListScreen(
                        state = state,
                        onCoinClick = {coin ->
                            Toast.makeText(context, "Clic en: ${coin.name}", Toast.LENGTH_SHORT).show()
                        },
                        onRetryClick = {
                            viewModel.loadCoins()
                        }
                    )
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