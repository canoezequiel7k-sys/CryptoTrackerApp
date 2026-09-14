package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import com.cursokotlin.crypto_tracker_app.ui.theme.Crypto_tracker_appTheme
import com.cursokotlin.crypto_tracker_app.ui.theme.greenPositive
import com.cursokotlin.crypto_tracker_app.ui.theme.redNegative
import java.util.Locale.US

@Composable
fun CoinListItem(
    coin: CryptoCoin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositive = coin.changePercent24Hr >= 0
    val changeColor =
        if (isPositive) {
            MaterialTheme.colorScheme.greenPositive
        } else {
            MaterialTheme.colorScheme.redNegative
        }

    Card(
        modifier = modifier
            .fillMaxWidth() //Usamos el maximo espacio en la anchura
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp), //Un radio en las esquinas
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) //Sombra 3d suave
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp), //un padding horizontal y vertical
            verticalAlignment = Alignment.CenterVertically //Que se alineen verticalmente los elementos que estan dentro
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                //----Logo de la cryptoMoneda usando Coil AsyncImage
                //es un componente de Coil para cargar imagenes de internet
                AsyncImage(
                    //fuente de la imagen
                    model = coin.iconUrl,
                    contentDescription = coin.name,
                    modifier = Modifier
                        .size(30.dp)
                )
            }

            //Estoy diciendole que deje un espacio horizontalmente
            Spacer(modifier = Modifier.width(12.dp))

            //Nombre y simbolo de la crypo moneda(Alinieados verticalmente en Colum)
            Column() {
                Text(
                    text = coin.name, //El nombre de cada CryptoMoneda
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, //Letra en negrita
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = coin.symbol, //Symbol de la CryptoMoneda
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            //Empuja los siguientes elementos hacia la derecha
            Spacer(modifier = Modifier.weight(1f)) //le dice ocupá el espacio disponible que quede

            //Precio y porcentaje de Variacion 24hs
            Column(
                //Alinea horizontalmente los elementos hacia el final de la Column
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${String.format(US, "%.2f", coin.priceUsd)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                //Etiqueta de porcentaje con fondo suave
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(changeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 2.dp)
                ){
                    Text(
                        //Si es true, agrega + al numero, si es falso el -(el numbrero ya tiene el simbolo -)
                        text = "${if (isPositive) "▲ +" else "▼"}${String.format(US, "%.2f", coin.changePercent24Hr)}%",
                        color = changeColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// Preview imagina que tenemos Bitcoin con estos datos.
@Preview
@Composable
private fun CoinListItemPreview() {
    Crypto_tracker_appTheme {
        CoinListItem(
            coin = CryptoCoin(
                id = "bitcoin",
                rank = 1,
                name = "Bitcoin",
                symbol = "BTC",
                priceUsd = 77259.00,
                changePercent24Hr = 2.35,
                iconUrl = "https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png"
            ),
            onClick = {}
        )
    }
}
