package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun LineChart(
    data: List<Double>,
    graphColor: Color,
    onPriceSelected: (Double?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val minPrice = data.minOrNull() ?: 0.0
    val maxPrice = data.maxOrNull() ?: 1.0
    val priceRange = if (maxPrice == minPrice) 1.0 else maxPrice - minPrice

    //Estado local para saber en que posicion X tiene presionado el dedo el usuario
    var selectedX by remember { mutableStateOf<Float?>(null) }
    var selectedPrice by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            //Muestra el precio exacto en el punto de toque
            selectedPrice?.let { price ->
                Text(
                    text = "Precio en ese punto: $${String.format(Locale.US, "%.2f", price)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }



    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            //Captura de GESTOS con pointerinput
            .pointerInput(data) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val index = (offset.x / size.width * (data.size - 1))
                            .toInt()
                            .coerceIn(0, data.size - 1)
                        selectedX = offset.x
                        selectedPrice = data[index]
                        onPriceSelected(data[index])
                    },
                    onDrag = { change, _ ->
                        val index = (change.position.x / size.width * (data.size - 1))
                            .toInt()
                            .coerceIn(0, data.size - 1)
                        selectedX = change.position.x
                        selectedPrice = data[index]
                        onPriceSelected(data[index])
                    },
                    onDragEnd = {
                        selectedX = null
                        selectedPrice = null
                        onPriceSelected(null)
                    },
                    onDragCancel = {
                        selectedX = null
                        selectedPrice = null
                        onPriceSelected(null)
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height

        //Puntos de coordenadas en el lienzo
        val path = Path()
        val fillPath = Path()

        val stepX = width / (data.size - 1)

        data.forEachIndexed { index, price ->
            val x = index * stepX
            //normalizamos la coordenada y entre 0 y la altura del lienzo
            val normalizedY = ((price - minPrice) / priceRange).toFloat()
            val y = height - (normalizedY * height)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.moveTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            if (index == data.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        //Dibujamos el degradado tenie debajo de la curva
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    graphColor.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        )

        //Dibujo la linea principal de la curva
        drawPath(
            path = path,
            color = graphColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // 4. INDICADOR VISUAL AL MANTENER EL DEDO PRESIONADO
        selectedX?.let { currentX ->
            val index = (currentX / width * (data.size - 1))
                .toInt()
                .coerceIn(0, data.size - 1)
            val priceAtX = data[index]
            val normalizedY = ((priceAtX - minPrice) / priceRange).toFloat()
            val currentY = height - (normalizedY * height)

            // Línea vertical punteada
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(currentX, 0f),
                end = Offset(currentX, height),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            // Ponto blanco y de color en la intersección de la curva
            drawCircle(
                color = Color.White,
                radius = 8.dp.toPx(),
                center = Offset(currentX, currentY)
            )
            drawCircle(
                color = graphColor,
                radius = 5.dp.toPx(),
                center = Offset(currentX, currentY)
            )
        }
    }
}