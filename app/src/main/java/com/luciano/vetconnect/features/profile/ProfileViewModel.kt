package com.luciano.vetconnect.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.Review
import com.luciano.vetconnect.shared.data.models.User
import com.luciano.vetconnect.shared.data.models.Favorite
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
                val reviewsResult = repository.getReviewsForUser(currentUser.id)
                val favoritesResult = repository.getFavoritesForUser(currentUser.id)

                when {
                    reviewsResult is ApiResult.Success && favoritesResult is ApiResult.Success -> {
                        _profileState.value = ProfileState.Success(
                            user = currentUser,
                            reviews = reviewsResult.data,
                            favorites = favoritesResult.data
                        )
                    }
                    reviewsResult is ApiResult.Error -> {
                        _profileState.value = ProfileState.Error(reviewsResult.message)
                    }
                    favoritesResult is ApiResult.Error -> {
                        _profileState.value = ProfileState.Error(favoritesResult.message)
                    }
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}