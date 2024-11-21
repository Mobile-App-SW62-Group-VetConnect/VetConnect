package com.luciano.vetconnect.features.veterinary.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.models.backendmodels.BusinessHour
import com.luciano.vetconnect.shared.data.models.backendmodels.UpdateVetCenterRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterResponse
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class VetEditProfileState {
    object Loading : VetEditProfileState()
    data class Success(val vetInfo: VetCenterResponse) : VetEditProfileState()
    data class Error(val message: String) : VetEditProfileState()
}

sealed class UpdateState {
    object Initial : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    data class Error(val message: String) : UpdateState()
}

class VetEditProfileViewModel(
    private val repository: VeterinaryRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<VetEditProfileState>(VetEditProfileState.Loading)
    val profileState: StateFlow<VetEditProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Initial)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

    init {
        loadVetProfile()
    }

    private fun loadVetProfile() {
        viewModelScope.launch {
            _profileState.value = VetEditProfileState.Loading

            try {
                val token = UserManager.getToken()
                val vetCenterId = UserManager.getVetCenterId()

                if (token == null || vetCenterId == null) {
                    _profileState.value = VetEditProfileState.Error("No se encontró la información necesaria")
                    return@launch
                }

                val result = repository.getVetInfobyId(vetCenterId, token)
                result.fold(
                    onSuccess = { vetInfo ->
                        _profileState.value = VetEditProfileState.Success(vetInfo)
                    },
                    onFailure = { exception ->
                        _profileState.value = VetEditProfileState.Error(
                            exception.message ?: "Error al cargar el perfil"
                        )
                    }
                )
            } catch (e: Exception) {
                _profileState.value = VetEditProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun updateProfile(
        name: String?,
        email: String?,
        ruc: String?,
        phone: String?,
        description: String?,
        address: String?,
        businessHours: List<BusinessHour>?
    ) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading

            try {
                val token = UserManager.getToken()
                val vetCenterId = UserManager.getVetCenterId()

                if (token == null || vetCenterId == null) {
                    _updateState.value = UpdateState.Error("No se encontró la información necesaria")
                    return@launch
                }

                val updateRequest = UpdateVetCenterRequest(
                    name = name,
                    email = email,
                    ruc = ruc,
                    phone = phone,
                    description = description,
                    address = address,
                    businessHours = businessHours,
                    imageProfile = null // Por ahora no manejamos la imagen
                )

                val result = repository.updateVetCenter(vetCenterId, updateRequest, token)

                result.fold(
                    onSuccess = { updatedVetInfo ->
                        // Actualizamos la información guardada
                        UserManager.setVetCenterInfo(updatedVetInfo)
                        _updateState.value = UpdateState.Success
                    },
                    onFailure = { exception ->
                        _updateState.value = UpdateState.Error(
                            exception.message ?: "Error al actualizar el perfil"
                        )
                    }
                )
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UpdateState.Initial
    }

    companion object {
        fun provideFactory(
            repository: VeterinaryRepository = VeterinaryRepository.getInstanceReal()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VetEditProfileViewModel(repository) as T
            }
        }
    }
}