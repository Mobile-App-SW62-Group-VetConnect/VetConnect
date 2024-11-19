package com.luciano.vetconnect.shared.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.api.RetrofitInstance
import com.luciano.vetconnect.shared.data.api.safeApiCall
import com.luciano.vetconnect.shared.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class VeterinaryRepository {
    private val api = RetrofitInstance.api

    // Mock Login
    private suspend fun mockLogin(email: String, password: String): ApiResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUsers()

                // Buscar en credenciales de clientes
                val clientCredential = response.credentials.clients.find {
                    it.email == email && it.password == password
                }
                if (clientCredential != null) {
                    val user = response.users.find { it.id == clientCredential.userId }
                    return@withContext if (user != null) {
                        ApiResult.Success(LoginResponse("mock-token", user))
                    } else {
                        ApiResult.Error("Usuario no encontrado")
                    }
                }

                // Buscar en credenciales de veterinarias
                val vetCredential = response.credentials.veterinaries.find {
                    it.email == email && it.password == password
                }
                if (vetCredential != null) {
                    val user = response.users.find { it.id == vetCredential.userId }
                    return@withContext if (user != null) {
                        ApiResult.Success(LoginResponse("mock-token", user))
                    } else {
                        ApiResult.Error("Usuario no encontrado")
                    }
                }

                ApiResult.Error("Credenciales inválidas")
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Veterinarias
    suspend fun getVeterinaries(): ApiResult<List<Veterinary>> = withContext(Dispatchers.IO) {
        safeApiCall {
            api.getMockVeterinaries().veterinaries
        }
    }

    suspend fun getVeterinaryById(id: String): ApiResult<Veterinary> = withContext(Dispatchers.IO) {
        safeApiCall {
            val response = api.getMockVeterinaries()
            response.veterinaries.find { it.id == id }
                ?: throw Exception("Veterinaria no encontrada")
        }
    }

    suspend fun getServicesForVeterinary(veterinaryId: String): ApiResult<List<VeterinaryService>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                val response = api.getMockServices()
                response.services.filter { it.veterinaryId == veterinaryId }
            }
        }



    suspend fun getReviewsForVeterinary(veterinaryId: String): ApiResult<List<Review>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                api.getMockReviews().reviews.filter { it.veterinaryId == veterinaryId }
            }
        }

    suspend fun getReviewsForUser(userId: String): ApiResult<List<Review>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                api.getMockReviews().reviews.filter { it.userId == userId }
            }
        }

    // Login y autenticación
    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return mockLogin(email, password)
    }

    // Detalle completo de veterinaria
    suspend fun getVeterinaryWithDetails(veterinaryId: String): ApiResult<VeterinaryWithDetails> =
        withContext(Dispatchers.IO) {
            try {
                val veterinaryResult = getVeterinaryById(veterinaryId)
                val servicesResult = getServicesForVeterinary(veterinaryId)
                val reviewsResult = getReviewsForVeterinary(veterinaryId)

                when {
                    veterinaryResult is ApiResult.Success &&
                            servicesResult is ApiResult.Success &&
                            reviewsResult is ApiResult.Success -> {
                        ApiResult.Success(
                            VeterinaryWithDetails(
                                veterinary = veterinaryResult.data,
                                services = servicesResult.data,
                                reviews = reviewsResult.data
                            )
                        )
                    }
                    veterinaryResult is ApiResult.Error -> veterinaryResult
                    servicesResult is ApiResult.Error -> servicesResult
                    reviewsResult is ApiResult.Error -> reviewsResult
                    else -> ApiResult.Error("Error desconocido al obtener los datos")
                }
            } catch (e: Exception) {
                ApiResult.Error(e.message ?: "Error desconocido")
            }
        }

    // Búsqueda y filtrado
    suspend fun searchVeterinaries(
        query: String? = null,
        filterOptions: FilterOptions? = null
    ): ApiResult<List<Veterinary>> = withContext(Dispatchers.IO) {
        safeApiCall {
            val allVeterinaries = api.getMockVeterinaries().veterinaries

            allVeterinaries.filter { veterinary ->
                var matches = true

                if (!query.isNullOrBlank()) {
                    matches = matches && (veterinary.name.contains(query, ignoreCase = true) ||
                            veterinary.address.contains(query, ignoreCase = true))
                }

                if (filterOptions != null) {
                    matches = matches &&
                            veterinary.rating >= filterOptions.minRating &&
                            (filterOptions.maxPrice == Int.MAX_VALUE ||
                                    getAveragePrice(veterinary.id) <= filterOptions.maxPrice)
                }

                matches
            }
        }
    }

     suspend fun getFavoritesForUser(userId: String): ApiResult<List<Favorite>> {
    return try {
        val response = api.getFavoritesForUser()
        if (response.isSuccessful && response.body() != null) {
            // Filtramos los favoritos por userId
            val filteredFavorites = response.body()!!.favorites.filter { it.userId == userId }
            ApiResult.Success(filteredFavorites)
        } else {
            ApiResult.Error("Error al obtener los favoritos")
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Error desconocido")
    }
}

    // Utilidades
    private suspend fun getAveragePrice(veterinaryId: String): Double {
        return when (val result = getServicesForVeterinary(veterinaryId)) {
            is ApiResult.Success -> {
                if (result.data.isEmpty()) 0.0
                else result.data.map { it.price }.average()
            }
            is ApiResult.Error -> 0.0
        }
    }

    companion object {
        @Volatile
        private var instance: VeterinaryRepository? = null

        fun getInstance(): VeterinaryRepository {
            return instance ?: synchronized(this) {
                instance ?: VeterinaryRepository().also { instance = it }
            }
        }
    }
}