package com.luciano.vetconnect.features.client.vet_information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.VeterinaryWithDetails
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VetDetailViewModel : ViewModel() {
    private val repository = VeterinaryRepository.getInstance()

    private val _uiState = MutableStateFlow<VetDetailUiState>(VetDetailUiState.Loading)
    val uiState: StateFlow<VetDetailUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var currentVeterinaryId: String? = null

    fun loadVeterinaryDetails(veterinaryId: String) {
        if (currentVeterinaryId == veterinaryId && _uiState.value is VetDetailUiState.Success) {
            return
        }

        currentVeterinaryId = veterinaryId
        _uiState.value = VetDetailUiState.Loading

        viewModelScope.launch {
            try {
                when (val result = repository.getVeterinaryWithDetails(veterinaryId)) {
                    is ApiResult.Success -> {
                        _uiState.value = VetDetailUiState.Success(
                            data = result.data,
                            isFavorite = false // TODO: Implementar estado real de favoritos
                        )
                    }
                    is ApiResult.Error -> {
                        _uiState.value = VetDetailUiState.Error(
                            result.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = VetDetailUiState.Error(
                    e.message ?: "Error desconocido al cargar los datos"
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentVeterinaryId?.let { loadVeterinaryDetails(it) }
            _isRefreshing.value = false
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is VetDetailUiState.Success) {
            _uiState.value = currentState.copy(isFavorite = !currentState.isFavorite)
        }
    }
}

sealed class VetDetailUiState {
    object Loading : VetDetailUiState()
    data class Success(
        val data: VeterinaryWithDetails?,
        val isFavorite: Boolean
    ) : VetDetailUiState()
    data class Error(val message: String) : VetDetailUiState()
}