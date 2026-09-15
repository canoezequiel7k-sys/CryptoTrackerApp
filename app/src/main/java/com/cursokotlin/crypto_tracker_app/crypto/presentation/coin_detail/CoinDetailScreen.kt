package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_detail.components.CryptoConverterCard
import com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.components.LineChart
import com.cursokotlin.crypto_tracker_app.ui.theme.greenPositive
import com.cursokotlin.crypto_tracker_app.ui.theme.redNegative
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    coin: CryptoCoin?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
){
    if (coin == null) return

    val isPositive = coin.changePercent24Hr >= 0

    val changeColor = if (isPositive){
        MaterialTheme.colorScheme.greenPositive
    }else{
        MaterialTheme.colorScheme.redNegative
    }

    //Estado local para intervalo de tiempo seleccionado
    var selectedTimeFrame by remember { mutableStateOf("7d") }
    val timeFrame = listOf("24h", "7d", "1m", "3m")

    //Filtramos los datos del grafico segun el intervalo seleccionado
    val displayedChartData = remember(selectedTimeFrame, coin.priceHistory) {
        when (selectedTimeFrame) {
            "24h" -> coin.priceHistory.takeLast(24) // Últimos 24 puntos
            "7d" -> coin.priceHistory              // Todos los 168 puntos de 7 días
            "1m" -> coin.priceHistory.takeLast(72)  // Subconjunto de puntos
            "3m" -> coin.priceHistory.takeLast(120) // Subconjunto de puntos
            else -> coin.priceHistory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = coin.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    //Header: Logo y Nombre
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = coin.iconUrl,
                            contentDescription = coin.name,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column() {
                            Text(
                                text = "${coin.name} (${coin.symbol})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ranking #${coin.rank}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    //Precio y %
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", coin.priceUsd)}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(12.dp))


                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(changeColor.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "${if (isPositive) "+" else ""}${String.format(Locale.US, "%.2f", coin.changePercent24Hr)}%",
                                color = changeColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Widget del Gráfico con SELECTOR DE INTERVALOS
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    //Titulo de la tendencia
                    Text(
                        text = "Tendencia de Precios (7 Dias)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        timeFrame.forEach { frame ->
                            val isSelected = frame == selectedTimeFrame

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .clickable {selectedTimeFrame = frame}
                                    .padding(horizontal = 16.dp)
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    text = frame,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    //Grafico de Linea
                    LineChart(
                        data = displayedChartData,
                        graphColor = changeColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Calculadora / Conversora a USD
            CryptoConverterCard(
                coin = coin
            )
        }
    }
}