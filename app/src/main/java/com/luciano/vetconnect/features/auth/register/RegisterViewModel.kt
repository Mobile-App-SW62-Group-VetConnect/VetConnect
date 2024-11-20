import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.models.backendmodels.SignUpRequest
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val repository: VeterinaryRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    // Agregamos el método setError que faltaba
    fun setError(message: String) {
        _registerState.value = RegisterState.Error(message)
    }

    // Agregamos método para resetear el estado
    fun resetState() {
        _registerState.value = RegisterState.Initial
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        // Mínimo 8 caracteres, al menos una letra y un número
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()
        return password.matches(passwordPattern)
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        return phone.length == 9 && phone.all { it.isDigit() }
    }

    private fun validateDNI(dni: String): Boolean {
        return dni.length == 8 && dni.all { it.isDigit() }
    }

    private fun validateRUC(ruc: String): Boolean {
        return ruc.length == 11 && ruc.all { it.isDigit() }
    }

    fun registerClient(
        email: String,
        password: String,
        name: String,
        dni: String,
        phone: String,
        address: String? = null
    ) {
        // Validaciones
        when {
            !validateEmail(email) -> {
                setError("Correo electrónico inválido")
                return
            }
            !validatePassword(password) -> {
                setError("La contraseña debe tener al menos 8 caracteres, una letra y un número")
                return
            }
            name.isBlank() -> {
                setError("El nombre es requerido")
                return
            }
            !validateDNI(dni) -> {
                setError("DNI inválido")
                return
            }
            !validatePhoneNumber(phone) -> {
                setError("Número de teléfono inválido")
                return
            }
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            try {
                val signUpRequest = SignUpRequest(
                    email = email,
                    password = password,
                    roles = listOf("CLIENT"),
                    name = name,
                    dni = dni,
                    phone = phone,
                    address = address
                )

                val result = repository.signUp(signUpRequest)
                result.fold(
                    onSuccess = {
                        _registerState.value = RegisterState.Success("Registro exitoso")
                    },
                    onFailure = { exception ->
                        _registerState.value = RegisterState.Error(
                            exception.message ?: "Error al registrar usuario"
                        )
                    }
                )
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun registerVeterinary(
        email: String,
        password: String,
        clinicName: String,
        ruc: String,
        license: String,
        address: String,
        phone: String
    ) {
        // Validaciones
        when {
            !validateEmail(email) -> {
                setError("Correo electrónico inválido")
                return
            }
            !validatePassword(password) -> {
                setError("La contraseña debe tener al menos 8 caracteres, una letra y un número")
                return
            }
            clinicName.isBlank() -> {
                setError("El nombre de la clínica es requerido")
                return
            }
            !validateRUC(ruc) -> {
                setError("RUC inválido")
                return
            }
            license.isBlank() -> {
                setError("El número de licencia es requerido")
                return
            }
            !validatePhoneNumber(phone) -> {
                setError("Número de teléfono inválido")
                return
            }
            address.isBlank() -> {
                setError("La dirección es requerida")
                return
            }
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            try {
                val signUpRequest = SignUpRequest(
                    email = email,
                    password = password,
                    roles = listOf("VETERINARY"),
                    vetCenterClinicName = clinicName,
                    vetCenterRuc = ruc,
                    vetCenterLicense = license,
                    vetCenterAddress = address,
                    vetCenterPhone = phone
                )

                val result = repository.signUp(signUpRequest)
                result.fold(
                    onSuccess = {
                        _registerState.value = RegisterState.Success("Registro exitoso")
                    },
                    onFailure = { exception ->
                        _registerState.value = RegisterState.Error(
                            exception.message ?: "Error al registrar veterinaria"
                        )
                    }
                )
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: VeterinaryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(repository) as T
            }
        }
    }
}