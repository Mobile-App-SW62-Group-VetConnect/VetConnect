package com.luciano.vetconnect.shared.data.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.luciano.vetconnect.shared.data.models.*
import com.luciano.vetconnect.shared.data.models.auth.AuthResponse
import com.luciano.vetconnect.shared.data.models.auth.SignInRequest
import com.luciano.vetconnect.shared.data.models.auth.SignUpRequest
import kotlinx.datetime.LocalDateTime
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    //Autenticación
    @POST(ApiConfig.SIGN_IN_URL)
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST(ApiConfig.SIGN_UP_URL)
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    // Veterinarias
    @GET("bd5e139a-4409-476e-b15e-fa67be717a7e")
    suspend fun getMockVeterinaries(): VeterinaryResponse

    // Servicios
    @GET("b66e9954-1966-490f-9168-6556b5ece9ea")
    suspend fun getMockServices(): ServiceResponse

    // Reseñas
    @GET("c3b75cf5-064a-4fb0-942a-c5508b1858c3")
    suspend fun getMockReviews(): ReviewResponse

    // Usuarios
    @GET("6eec656c-acb4-4f28-a927-fe8111d2acd7")
    suspend fun getUsers(): UserResponse

    // Favoritos
    @GET("646ff380-c8ce-4123-83fc-8a9331bfe01e")
    suspend fun getFavoritesForUser(): Response<FavoritesResponse>
}

object RetrofitInstance {
    //private val mockApi by lazy {
   //     createRetrofit(ApiConfig.BASE_URL).create(ApiService::class.java)
   // }

   // private val realApi by lazy {
  //      createRetrofit(ApiConfig.APIBASE_URL).create(ApiService::class.java)
   // }

    fun getMockApi(): ApiService = createRetrofit(ApiConfig.BASE_URL).create(ApiService::class.java)
    fun getRealApi(): ApiService = createRetrofit(ApiConfig.APIBASE_URL).create(ApiService::class.java)

    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        ApiResult.Error(
            when (throwable) {
                is java.net.UnknownHostException -> "No hay conexión a internet"
                is java.net.SocketTimeoutException -> "Tiempo de espera agotado"
                is retrofit2.HttpException -> when(throwable.code()) {
                    401 -> "No autorizado"
                    403 -> "Acceso denegado"
                    404 -> "Recurso no encontrado"
                    500 -> "Error del servidor"
                    else -> "Error de red (${throwable.code()})"
                }
                else -> throwable.message ?: "Error desconocido"
            }
        )
    }
}