package com.luciano.vetconnect.shared.data.api

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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    // Authentication Endpoints
    @POST(ApiConfig.SIGN_IN_URL)
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST(ApiConfig.SIGN_UP_URL)
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @PUT(ApiConfig.CHANGE_PASSWORD_URL)
    suspend fun changePassword(
        @Path("userId") userId: Long,
        @Body request: ChangePasswordRequest,
        @Header("Authorization") token: String
    ): Response<UserResponse>

    // User Endpoints
    @GET(ApiConfig.GET_ALL_USERS_URL)
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<UserResponse>>

    @GET(ApiConfig.GET_USER_BY_ID_URL)
    suspend fun getUserById(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<UserResponse>


    // Veterinary Center Endpoints
    @GET(ApiConfig.GET_ALL_VET_CENTERS_URL)
    suspend fun getAllVetCenters(
        @Header("Authorization") token: String
    ): Response<List<VetCenterResponse>>

    @GET(ApiConfig.GET_VET_CENTER_BY_ID_URL)
    suspend fun getVetInfobyId(
        @Path("vetCenterId") vetCenterId: Long,
        @Header("Authorization") token: String
    ): Response<VetCenterResponse>

    @GET(ApiConfig.GET_VET_CENTER_BY_NAME_URL)
    suspend fun getVetCenterByName(
        @Path("vetCenterName") vetCenterName: String,
        @Header("Authorization") token: String
    ): Response<VetCenterResponse>

    @PUT(ApiConfig.UPDATE_VET_CENTER_URL)
    suspend fun updateVetCenter(
        @Path("vetCenterId") vetCenterId: Long,
        @Body request: UpdateVetCenterRequest,
        @Header("Authorization") token: String
    ): Response<VetCenterResponse>

    @GET(ApiConfig.GET_VET_CENTER_IMAGES_URL)
    suspend fun getVetCenterImages(
        @Path("vetCenterId") vetCenterId: Long,
        @Header("Authorization") token: String
    ): Response<List<VetCenterImageResponse>>

    @POST(ApiConfig.ADD_VET_CENTER_IMAGE_URL)
    suspend fun addVetCenterImage(
        @Path("vetCenterId") vetCenterId: Long,
        @Body request: AddVetCenterImageRequest,
        @Header("Authorization") token: String
    ): Response<String>

    // Pet Owner Endpoints
    @POST(ApiConfig.CREATE_PET_OWNER_URL)
    suspend fun createPetOwner(
        @Body request: CreatePetOwnerRequest,
        @Header("Authorization") token: String
    ): Response<PetOwnerResponse>

    @GET(ApiConfig.GET_ALL_PET_OWNERS_URL)
    suspend fun getAllPetOwners(
        @Header("Authorization") token: String
    ): Response<List<PetOwnerResponse>>

    @GET(ApiConfig.GET_PET_OWNER_BY_ID_URL)
    suspend fun getPetOwnerById(
        @Path("petOwnerId") petOwnerId: Long,
        @Header("Authorization") token: String
    ): Response<PetOwnerResponse>

    @PUT(ApiConfig.UPDATE_PET_OWNER_URL)
    suspend fun updatePetOwner(
        @Path("petOwnerId") petOwnerId: Long,
        @Body request: UpdatePetOwnerRequest,
        @Header("Authorization") token: String
    ): Response<PetOwnerResponse>

    // Reviews Endpoints
    @POST(ApiConfig.CREATE_REVIEW_URL)
    suspend fun createReview(
        @Body request: CreateReviewRequest,
        @Header("Authorization") token: String
    ): Response<ReviewResponse>

    @GET(ApiConfig.GET_ALL_REVIEWS_URL)
    suspend fun getAllReviews(
        @Header("Authorization") token: String
    ): Response<List<ReviewResponse>>

    @GET(ApiConfig.GET_REVIEWS_BY_VET_CENTER_URL)
    suspend fun getReviewsByVetCenter(
        @Path("vetCenterId") vetCenterId: Long,
        @Header("Authorization") token: String
    ): Response<List<ReviewResponse>>

    @DELETE(ApiConfig.DELETE_REVIEW_URL)
    suspend fun deleteReview(
        @Query("reviewId") reviewId: Long,
        @Header("Authorization") token: String
    ): Response<Unit>

    // Vet Services Endpoints
    @POST(ApiConfig.CREATE_VET_SERVICE_URL)
    suspend fun createVetService(
        @Body request: CreateVetServiceRequest,
        @Header("Authorization") token: String
    ): Response<VetServiceResponse>

    @GET(ApiConfig.GET_ALL_VET_SERVICES_URL)
    suspend fun getAllVetServices(
        @Header("Authorization") token: String
    ): Response<List<VetServiceResponse>>

    @GET(ApiConfig.GET_VET_SERVICE_BY_ID_URL)
    suspend fun getVetServiceById(
        @Path("vetServiceId") vetServiceId: Long,
        @Header("Authorization") token: String
    ): Response<VetServiceResponse>

    @GET(ApiConfig.GET_VET_SERVICES_BY_VET_CENTER_URL)
    suspend fun getVetServicesByVetCenter(
        @Path("vetCenterId") vetCenterId: Long,
        @Header("Authorization") token: String
    ): Response<List<VetServiceResponse>>

    @PUT(ApiConfig.UPDATE_VET_SERVICE_URL)
    suspend fun updateVetService(
        @Path("vetServiceId") vetServiceId: Long,
        @Body request: UpdateVetServiceRequest,
        @Header("Authorization") token: String
    ): Response<VetServiceResponse>

    @DELETE(ApiConfig.DELETE_VET_SERVICE_URL)
    suspend fun deleteVetService(
        @Path("vetServiceId") vetServiceId: Long,
        @Header("Authorization") token: String
    ): Response<Unit>

    // Favorites Endpoints
    @POST(ApiConfig.CREATE_FAVORITE_URL)
    suspend fun createFavorite(
        @Body request: CreateFavoriteRequest,
        @Header("Authorization") token: String
    ): Response<FavoriteResponse>

    @GET(ApiConfig.GET_ALL_FAVORITES_URL)
    suspend fun getAllFavorites(
        @Header("Authorization") token: String
    ): Response<List<FavoriteResponse>>

    @GET(ApiConfig.GET_FAVORITE_BY_ID_URL)
    suspend fun getFavoriteById(
        @Path("favoriteId") favoriteId: Long,
        @Header("Authorization") token: String
    ): Response<FavoriteResponse>

    @GET(ApiConfig.GET_FAVORITES_BY_USER_URL)
    suspend fun getFavoritesByUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<FavoriteResponse>

    @DELETE(ApiConfig.DELETE_FAVORITE_URL)
    suspend fun deleteFavorite(
        @Path("favoriteId") favoriteId: Long,
        @Header("Authorization") token: String
    ): Response<Unit>


    //MOCKAPI ENDPOINTS
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