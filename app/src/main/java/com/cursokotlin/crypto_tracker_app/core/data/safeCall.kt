package com.cursokotlin.crypto_tracker_app.core.data

import com.cursokotlin.crypto_tracker_app.core.util.NetworkError
import com.cursokotlin.crypto_tracker_app.core.util.Result
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

//Para no repetir bloques try-catch feos en cada llamada a la API, crearemos una función auxiliar genérica de ejecución segura.
suspend inline fun <reified T> safeCall(
    execute: () -> Response<T>
): Result<T, NetworkError>{
    //atrapa excepciones físicas
    val response = try {
        execute()
    //Manejp de errores Nivel Conexión
    }catch (e: SocketTimeoutException){
        return Result.Error(NetworkError.REQUEST_TIMEOUT)
    }catch (e: IOException){
        return Result.Error(NetworkError.NO_INTERNET)
    }catch (e: Exception){
        return Result.Error(NetworkError.UNKNOWN)
    }
    return responseToResult(response)
}


//Manejo de errores Nivel Servidor
fun <T> responseToResult(response: Response<T>): Result<T, NetworkError>{
    //Analiza el código HTTP de respuesta
    return when(response.code()){
        in 200..299 ->{
            val body = response.body()
            if (body != null){
                Result.Success(body)
            }else{
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}