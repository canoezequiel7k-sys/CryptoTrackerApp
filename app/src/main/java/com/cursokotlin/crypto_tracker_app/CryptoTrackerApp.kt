package com.cursokotlin.crypto_tracker_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Disparador principal de Hilt. Le indica a Dagger Hilt que genere el contenedor de dependencias
@HiltAndroidApp
//Hereda de Application. Es el primer código que se ejecuta en memoria cuando el usuario toca el ícono de la app.
class CryptoTrackerApp : Application()