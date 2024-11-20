package com.luciano.vetconnect.features.client.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.Review
import com.luciano.vetconnect.shared.data.models.User
import com.luciano.vetconnect.shared.data.models.Favorite
import com.luciano.vetconnect.shared.data.models.UserRole
import com.luciano.vetconnect.shared.data.models.backendmodels.AuthResponse
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.utils.UserManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
        val user: User,
        val reviews: List<Review>,
        val favorites: List<Favorite>
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel : ViewModel() {
    private val repository = VeterinaryRepository.getInstance()
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            val currentUser = UserManager.currentUser.value
            if (currentUser == null) {
                _profileState.value = ProfileState.Error("Usuario no encontrado")
                return@launch
            }

            try {
                // Adaptamos los datos del AuthResponse a lo que espera el ProfileState
                val authUser = UserManager.currentUser.value!!
                val reviews = emptyList<Review>() // Por ahora, lista vacía
                val favorites = emptyList<Favorite>() // Por ahora, lista vacía

                _profileState.value = ProfileState.Success(
                    user = convertAuthResponseToUser(authUser),
                    reviews = reviews,
                    favorites = favorites
                )
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Función auxiliar para convertir AuthResponse a User
    private fun convertAuthResponseToUser(authResponse: AuthResponse): User {
        return User(
            id = authResponse.id.toString(),
            name = authResponse.username,
            email = authResponse.username,
            phone = "",
            address = null,
            imageUrl = null,
            role = UserRole.valueOf(authResponse.role), // Ahora solo puede ser CLIENT o VETERINARY
            veterinaryId = null,
            license = null,
            reviewCount = 0,
            favoriteCount = 0,
            createdAt = "2024-01-01T00:00:00",
            lastLoginAt = null
        )
    }
}