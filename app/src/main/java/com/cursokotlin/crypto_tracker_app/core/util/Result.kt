package com.cursokotlin.crypto_tracker_app.core.util

import androidx.compose.ui.graphics.drawscope.DrawTransform

//sealed interface solo puede ser implementada dentro del mismo archivo.

/**   Interface sellada para representar errores de res especificos.   */
sealed interface NetworkError {
    //DATA OBJECT singleton(unica instancia en memoria)
    data object REQUEST_TIMEOUT : NetworkError
    data object TOO_MANY_REQUESTS : NetworkError
    data object NO_INTERNET : NetworkError
    data object SERVER_ERROR : NetworkError
    data object SERIALIZATION : NetworkError
    data object UNKNOWN : NetworkError
}


/**   WRAPPER DE RESULTADO GENERICO PARA MANEJAR RESPUESTAS EXITOSAS O FALLIDAS   */
//out D, out E(varianza / covarianza) out indica tipo genericos D(Data) E(Error), solo van a ser (salida), nunca consumidos como parametros de entrada
//nos permite retornar como Result<Nothing E> en la clase de error, porque nothing es el subtipo de cualquier clase en kotlin
sealed interface Result<out D, out E> {
    //No hay error, pasa nothing como tipo de error
    data class Success<out D>(val data: D) : Result<D, Nothing>

    //No hay datos, pasa nothing como tipo de dato
    data class Error<out E>(val error: E) : Result<Nothing, E>
}


/**
 * Función de extensión que transforma el dato dentro de un [Result.Success]
 * dejando intacto el [Result.Error].
 */
inline fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        //Si el resultado actual es success, tomamos su campo data y se lo pasamos a la funcion de fransformacion y mapea cada DTO a un CryptoCoin y lo envolvemos en un nuevo Result.Success
        is Result.Success -> Result.Success(transform(data))
        //Si es un Error, no toca nada y devuelve el mismo Result.Error(error)
        is Result.Error -> Result.Error(error)
    }
}