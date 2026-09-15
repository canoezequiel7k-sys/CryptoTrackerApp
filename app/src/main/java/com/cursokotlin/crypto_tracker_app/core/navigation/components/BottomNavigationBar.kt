package com.cursokotlin.crypto_tracker_app.core.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cursokotlin.crypto_tracker_app.core.navigation.BottomBarTab
import com.cursokotlin.crypto_tracker_app.core.navigation.Route


@Composable
fun BottomNavigationBar(
    currentRoute: Route?,
    onTabSelected: (BottomBarTab) -> Unit,
    modifier: Modifier = Modifier
){
    //Contenedor principal estandar de Material 3 para la barra inferior
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        //Recorrido de las 3 opciones (home, favorites, profile) para generar los botones
        BottomBarTab.entries.forEach { tab ->
            //Comprobamos si esta pestaña es la pantalla activa actual
            val isSelected = currentRoute == tab.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {onTabSelected(tab)},
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) //Al selecciones, dibuja una "capsula" con una opacidad de nuestro azul
                )
            )
        }
    }
}