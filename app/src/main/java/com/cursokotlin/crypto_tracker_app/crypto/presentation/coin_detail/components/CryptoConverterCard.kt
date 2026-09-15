package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin
import java.util.Locale


//Componente que calcula la conversion en tiempo real de la criptomoneda a dolares
@Composable
fun CryptoConverterCard(
    coin: CryptoCoin,
    modifier: Modifier = Modifier
){
    //Estado local para la cantidad de cantidad ingresada por el usuario ( por defecto 1)
    var amountText by remember { mutableStateOf("1") }

    //Calculo en tiempo real
    val amount = amountText.toDoubleOrNull() ?: 0.0
    val totalUsd = amount * coin.priceUsd

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Convertir / Comparar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                //Campo de texto para ingresar la cantidad de la moneda
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {newValue ->
                        //Expresion regular para validar solo los numeros decimales
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))){
                            amountText = newValue
                        }
                    },
                    label = {Text("Cantidad (${coin.symbol})")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                // Resultado del equivalente en USD
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total en USD",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", totalUsd)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}