package com.cursokotlin.crypto_tracker_app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryCrypto,
    onPrimary = OnPrimaryCrypto,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryCrypto,
    onPrimary = OnPrimaryCrypto,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight
)

val ColorScheme.greenPositive: Color
@Composable
@ReadOnlyComposable
get() = if (isSystemInDarkTheme()) GreenPositive else GreenPositiveLight

val ColorScheme.redNegative: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) RedNegative else RedNegativeLight

@Composable
fun Crypto_tracker_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Para una app identica de marca de criptomonedas, desactivamos el dynamicColor por defecto
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}