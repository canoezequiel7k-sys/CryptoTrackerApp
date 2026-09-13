package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    data: List<Double>,
    graphColor: Color,
    modifier: Modifier = Modifier
    ){
    if (data.isEmpty()) return

    val minPrice = data.minOrNull() ?: 0.0
    val maxPrice = data.maxOrNull() ?: 1.0
    val priceRange = if (maxPrice == minPrice) 1.0 else maxPrice - minPrice


    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
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

            if (index == 0){
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.moveTo(x, y)
            }else{
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            if (index == data.size - 1){
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        //Dibujamos el degradado tenue debajo de la curva
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

    }



}