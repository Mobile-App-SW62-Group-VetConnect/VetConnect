package com.luciano.vetconnect.shared.data.repository

import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.api.ApiService
import com.luciano.vetconnect.shared.data.api.RetrofitInstance
import com.luciano.vetconnect.shared.data.api.safeApiCall
import com.luciano.vetconnect.shared.data.models.*
import com.luciano.vetconnect.shared.data.models.backendmodels.AddVetCenterImageRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.AuthResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.ChangePasswordRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.CreateFavoriteRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.CreatePetOwnerRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.CreateReviewRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.FavoriteResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.PetOwnerResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.SignInRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.SignUpRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.UpdatePetOwnerRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.UpdateVetCenterRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterImageResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterResponse
import com.luciano.vetconnect.shared.data.models.vetservices.CreateVetServiceRequest
import com.luciano.vetconnect.shared.data.models.vetservices.UpdateVetServiceRequest
import com.luciano.vetconnect.shared.data.models.vetservices.VetServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VeterinaryRepository private constructor(private val apiService: ApiService) {
    private val mockApi = RetrofitInstance.getMockApi()
    private val realApi = RetrofitInstance.getRealApi()

    // Authentication Methods
    suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = realApi.signIn(SignInRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error en la autenticación: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(request: SignUpRequest): Result<AuthResponse> {
        return try {
            val response = realApi.signUp(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error en el registro: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String, token: String): Result<UserResponse> {
        return try {
            val response = realApi.changePassword(
                userId,
                ChangePasswordRequest(oldPassword, newPassword),
                "Bearer $token"
            )
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // User Methods
    suspend fun getAllUsers(token: String): Result<List<UserResponse>> {
        return try {
            val response = realApi.getAllUsers("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Long, token: String): Result<UserResponse> {
        return try {
            val response = realApi.getUserById(userId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Vet Center Methods
    suspend fun getAllVetCenters(token: String): Result<List<VetCenterResponse>> {
        return try {
            val response = realApi.getAllVetCenters("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVetInfobyId(vetCenterId: Long, token: String): Result<VetCenterResponse> {
        return try {
            val response = realApi.getVetInfobyId(vetCenterId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVetCenterByName(vetCenterName: String, token: String): Result<VetCenterResponse> {
        return try {
            val response = realApi.getVetCenterByName(vetCenterName, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateVetCenter(vetCenterId: Long, request: UpdateVetCenterRequest, token: String): Result<VetCenterResponse> {
        return try {
            val response = realApi.updateVetCenter(vetCenterId, request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVetCenterImages(vetCenterId: Long, token: String): Result<List<VetCenterImageResponse>> {
        return try {
            val response = realApi.getVetCenterImages(vetCenterId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addVetCenterImage(vetCenterId: Long, request: AddVetCenterImageRequest, token: String): Result<String> {
        return try {
            val response = realApi.addVetCenterImage(vetCenterId, request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Pet Owner Methods
    suspend fun createPetOwner(request: CreatePetOwnerRequest, token: String): Result<PetOwnerResponse> {
        return try {
            val response = realApi.createPetOwner(request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPetOwners(token: String): Result<List<PetOwnerResponse>> {
        return try {
            val response = realApi.getAllPetOwners("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPetOwnerById(petOwnerId: Long, token: String): Result<PetOwnerResponse> {
        return try {
            val response = realApi.getPetOwnerById(petOwnerId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePetOwner(petOwnerId: Long, request: UpdatePetOwnerRequest, token: String): Result<PetOwnerResponse> {
        return try {
            val response = realApi.updatePetOwner(petOwnerId, request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Review Methods
    suspend fun createReview(request: CreateReviewRequest, token: String): Result<ReviewResponse> {
        return try {
            val response = realApi.createReview(request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllReviews(token: String): Result<List<ReviewResponse>> {
        return try {
            val response = realApi.getAllReviews("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByVetCenter(vetCenterId: Long, token: String): Result<List<ReviewResponse>> {
        return try {
            val response = realApi.getReviewsByVetCenter(vetCenterId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: Long, token: String): Result<Unit> {
        return try {
            val response = realApi.deleteReview(reviewId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Vet Service Methods
    suspend fun createVetService(request: CreateVetServiceRequest, token: String): Result<VetServiceResponse> {
        return try {
            val response = realApi.createVetService(request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllVetServices(token: String): Result<List<VetServiceResponse>> {
        return try {
            val response = realApi.getAllVetServices("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVetServiceById(vetServiceId: Long, token: String): Result<VetServiceResponse> {
        return try {
            val response = realApi.getVetServiceById(vetServiceId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVetServicesByVetCenter(vetCenterId: Long, token: String): Result<List<VetServiceResponse>> {
        return try {
            val response = realApi.getVetServicesByVetCenter(vetCenterId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateVetService(vetServiceId: Long, request: UpdateVetServiceRequest, token: String): Result<VetServiceResponse> {
        return try {
            val response = realApi.updateVetService(vetServiceId, request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteVetService(vetServiceId: Long, token: String): Result<Unit> {
        return try {
            val response = realApi.deleteVetService(vetServiceId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Favorite Methods
    suspend fun createFavorite(request: CreateFavoriteRequest, token: String): Result<FavoriteResponse> {
        return try {
            val response = realApi.createFavorite(request, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllFavorites(token: String): Result<List<FavoriteResponse>> {
        return try {
            val response = realApi.getAllFavorites("Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteById(favoriteId: Long, token: String): Result<FavoriteResponse> {
        return try {
            val response = realApi.getFavoriteById(favoriteId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoritesByUser(userId: Long, token: String): Result<FavoriteResponse> {
        return try {
            val response = realApi.getFavoritesByUser(userId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFavorite(favoriteId: Long, token: String): Result<Unit> {
        return try {
            val response = realApi.deleteFavorite(favoriteId, "Bearer $token")
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    //MOCKAPI
    // Login y autenticación con Mock
    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return mockLogin(email, password)
    }


    // Mock Login
    private suspend fun mockLogin(email: String, password: String): ApiResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mockApi.getUsers()

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
            mockApi.getMockVeterinaries().veterinaries
        }
    }

    suspend fun getVeterinaryById(id: String): ApiResult<Veterinary> = withContext(Dispatchers.IO) {
        safeApiCall {
            val response = mockApi.getMockVeterinaries()
            response.veterinaries.find { it.id == id }
                ?: throw Exception("Veterinaria no encontrada")
        }
    }

    suspend fun getServicesForVeterinary(veterinaryId: String): ApiResult<List<VeterinaryService>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                val response = mockApi.getMockServices()
                response.services.filter { it.veterinaryId == veterinaryId }
            }
        }



    suspend fun getReviewsForVeterinary(veterinaryId: String): ApiResult<List<Review>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                mockApi.getMockReviews().reviews.filter { it.veterinaryId == veterinaryId }
            }
        }

    suspend fun getReviewsForUser(userId: String): ApiResult<List<Review>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                mockApi.getMockReviews().reviews.filter { it.userId == userId }
            }
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
            val allVeterinaries = mockApi.getMockVeterinaries().veterinaries

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
        val response = mockApi.getFavoritesForUser()
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

    // Helper function to handle API responses
    private fun <T> handleResponse(response: retrofit2.Response<T>): Result<T> {
        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
        }
    }

    companion object {
        @Volatile
        private var mockinstance: VeterinaryRepository? = null
        @Volatile
        private var realInstance: VeterinaryRepository? = null

        fun getInstance(apiService: ApiService = RetrofitInstance.getMockApi()): VeterinaryRepository {
            return mockinstance ?: synchronized(this) {
                mockinstance ?: VeterinaryRepository(apiService).also { mockinstance = it }
            }
        }
        fun getInstanceReal(apiService: ApiService = RetrofitInstance.getRealApi()): VeterinaryRepository {
            return realInstance ?: synchronized(this) {
                realInstance ?: VeterinaryRepository(apiService).also { realInstance = it }
            }
        }
    }
}