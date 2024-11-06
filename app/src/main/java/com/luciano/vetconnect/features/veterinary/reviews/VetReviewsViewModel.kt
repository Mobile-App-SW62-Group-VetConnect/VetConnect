package com.luciano.vetconnect.features.veterinary.reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.Review
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ReviewsUiState {
    object Loading : ReviewsUiState()
    data class Success(val reviews: List<Review>) : ReviewsUiState()
    data class Error(val message: String) : ReviewsUiState()
}

class VetReviewsViewModel : ViewModel() {
    private val repository = VeterinaryRepository.getInstance()

    private val _uiState = MutableStateFlow<ReviewsUiState>(ReviewsUiState.Loading)
    val uiState: StateFlow<ReviewsUiState> = _uiState.asStateFlow()

    private val _selectedReview = MutableStateFlow<Review?>(null)
    val selectedReview: StateFlow<Review?> = _selectedReview.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadReviewsForVeterinary(veterinaryId: String) {
        viewModelScope.launch {
            _uiState.value = ReviewsUiState.Loading
            try {
                when (val result = repository.getReviewsForVeterinary(veterinaryId)) {
                    is ApiResult.Success -> {
                        _uiState.value = ReviewsUiState.Success(result.data)
                    }
                    is ApiResult.Error -> {
                        _uiState.value = ReviewsUiState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ReviewsUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadReviewById(reviewId: String) {
        viewModelScope.launch {
            when (val result = repository.getReviewsForVeterinary("vet-001")) {
                is ApiResult.Success -> {
                    _selectedReview.value = result.data.find { it.id == reviewId }
                }
                is ApiResult.Error -> {
                    // Manejar error
                }
            }
        }
    }

    fun clearSelectedReview() {
        _selectedReview.value = null
    }
}