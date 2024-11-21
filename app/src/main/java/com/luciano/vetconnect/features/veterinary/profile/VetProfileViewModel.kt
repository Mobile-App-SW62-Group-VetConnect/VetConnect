import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.models.backendmodels.BusinessHour
import com.luciano.vetconnect.shared.data.models.backendmodels.UpdateVetCenterRequest
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterImageResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterResponse
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class VetProfileState {
    object Loading : VetProfileState()
    data class Success(
        val vetInfo: VetCenterResponse,
        val images: List<VetCenterImageResponse> = emptyList()
    ) : VetProfileState()
    data class Error(val message: String) : VetProfileState()
}

sealed class UpdateProfileState {
    object Initial : UpdateProfileState()
    object Loading : UpdateProfileState()
    object Success : UpdateProfileState()
    data class Error(val message: String) : UpdateProfileState()
}

class VetProfileViewModel(
    private val repository: VeterinaryRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<VetProfileState>(VetProfileState.Loading)
    val profileState: StateFlow<VetProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Initial)
    val updateState: StateFlow<UpdateProfileState> = _updateState.asStateFlow()

    init {
        loadVetProfile()
    }

    fun loadVetProfile() {
        viewModelScope.launch {
            _profileState.value = VetProfileState.Loading

            try {
                val token = UserManager.getToken()
                if (token == null) {
                    _profileState.value = VetProfileState.Error("No se encontró el token de autenticación")
                    return@launch
                }

                val vetCenterId = UserManager.getVetCenterId()
                if (vetCenterId == null) {
                    // Si no tenemos el id guardado, necesitamos obtenerlo primero
                    val userId = UserManager.getUserId()
                    if (userId == null) {
                        _profileState.value = VetProfileState.Error("No se encontró la información del usuario")
                        return@launch
                    }

                    // Obtenemos la lista de veterinarias y buscamos la nuestra
                    val allVetCentersResult = repository.getAllVetCenters(token)
                    allVetCentersResult.fold(
                        onSuccess = { vetCenters ->
                            val vetCenter = vetCenters.find { it.user_id == userId }
                            if (vetCenter != null) {
                                // Guardamos la información de la veterinaria para futuras consultas
                                UserManager.setVetCenterInfo(vetCenter)
                                loadVetCenterDetails(vetCenter.id, token)
                            } else {
                                _profileState.value = VetProfileState.Error("No se encontró la información de la veterinaria")
                            }
                        },
                        onFailure = { exception ->
                            _profileState.value = VetProfileState.Error(
                                exception.message ?: "Error al cargar la información de la veterinaria"
                            )
                        }
                    )
                } else {
                    // Si ya tenemos el id, cargamos directamente los detalles
                    loadVetCenterDetails(vetCenterId, token)
                }
            } catch (e: Exception) {
                _profileState.value = VetProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private suspend fun loadVetCenterDetails(vetCenterId: Long, token: String) {
        val profileResult = repository.getVetInfobyId(vetCenterId, token)
        profileResult.fold(
            onSuccess = { vetInfo ->
                val imagesResult = repository.getVetCenterImages(vetCenterId, token)
                imagesResult.fold(
                    onSuccess = { images ->
                        _profileState.value = VetProfileState.Success(
                            vetInfo = vetInfo,
                            images = images
                        )
                    },
                    onFailure = {
                        // Si falla la carga de imágenes, mostramos el perfil sin imágenes
                        _profileState.value = VetProfileState.Success(vetInfo = vetInfo)
                    }
                )
            },
            onFailure = { exception ->
                _profileState.value = VetProfileState.Error(
                    exception.message ?: "Error al cargar el perfil"
                )
            }
        )
    }

    fun updateProfile(
        name: String? = null,
        email: String? = null,
        ruc: String? = null,
        phone: String? = null,
        description: String? = null,
        address: String? = null,
        businessHours: List<BusinessHour>? = null
    ) {
        viewModelScope.launch {
            _updateState.value = UpdateProfileState.Loading

            try {
                val token = UserManager.getToken()
                if (token == null) {
                    _updateState.value = UpdateProfileState.Error("No se encontró el token de autenticación")
                    return@launch
                }

                val vetId = UserManager.currentUser.value?.id ?: return@launch

                val updateRequest = UpdateVetCenterRequest(
                    name = name,
                    email = email,
                    ruc = ruc,
                    phone = phone,
                    description = description,
                    address = address,
                    businessHours = businessHours,
                    imageProfile = null // Se maneja separado
                )

                val result = repository.updateVetCenter(vetId, updateRequest, token)

                result.fold(
                    onSuccess = {
                        _updateState.value = UpdateProfileState.Success
                        loadVetProfile() // Recargamos el perfil para mostrar los cambios
                    },
                    onFailure = { exception ->
                        _updateState.value = UpdateProfileState.Error(
                            exception.message ?: "Error al actualizar el perfil"
                        )
                    }
                )
            } catch (e: Exception) {
                _updateState.value = UpdateProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: VeterinaryRepository = VeterinaryRepository.getInstanceReal()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VetProfileViewModel(repository) as T
            }
        }
    }
}