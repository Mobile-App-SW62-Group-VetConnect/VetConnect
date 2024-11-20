package com.luciano.vetconnect.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    data class Success(val isVetUser: Boolean) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val veterinaryRepository: VeterinaryRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun setError(message: String) {
        _registerState.value = RegisterState.Error(message)
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
        println("RegisterViewModel - Nombres: $name") // Log para verificar el valor de name
        // Validaciones
        when {
            !validateEmail(email) -> {
                _registerState.value = RegisterState.Error("Correo electrónico inválido")
                return
            }
            !validatePassword(password) -> {
                _registerState.value = RegisterState.Error("La contraseña debe tener al menos 8 caracteres, una letra y un número")
                return
            }
            name.isBlank() -> {
                _registerState.value = RegisterState.Error("El nombre es requerido")
                return
            }
            !validateDNI(dni) -> {
                _registerState.value = RegisterState.Error("DNI inválido")
                return
            }
            !validatePhoneNumber(phone) -> {
                _registerState.value = RegisterState.Error("Número de teléfono inválido")
                return
            }
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val result = veterinaryRepository.signUpClient(
                    email = email,
                    password = password,
                    name = name,
                    dni = dni,
                    phone = phone,
                    address = address
                )

                result.fold(
                    onSuccess = {
                        _registerState.value = RegisterState.Success(false)
                    },
                    onFailure = { exception ->
                        _registerState.value = RegisterState.Error(exception.message ?: "Error en el registro")
                    }
                )
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error en el registro")
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
                _registerState.value = RegisterState.Error("Correo electrónico inválido")
                return
            }
            !validatePassword(password) -> {
                _registerState.value = RegisterState.Error("La contraseña debe tener al menos 8 caracteres, una letra y un número")
                return
            }
            clinicName.isBlank() -> {
                _registerState.value = RegisterState.Error("El nombre de la clínica es requerido")
                return
            }
            !validateRUC(ruc) -> {
                _registerState.value = RegisterState.Error("RUC inválido")
                return
            }
            license.isBlank() -> {
                _registerState.value = RegisterState.Error("El número de licencia es requerido")
                return
            }
            !validatePhoneNumber(phone) -> {
                _registerState.value = RegisterState.Error("Número de teléfono inválido")
                return
            }
            address.isBlank() -> {
                _registerState.value = RegisterState.Error("La dirección es requerida")
                return
            }
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val result = veterinaryRepository.signUpVeterinary(
                    email = email,
                    password = password,
                    clinicName = clinicName,
                    ruc = ruc,
                    license = license,
                    address = address,
                    phone = phone
                )

                result.fold(
                    onSuccess = {
                        _registerState.value = RegisterState.Success(true)
                    },
                    onFailure = { exception ->
                        _registerState.value = RegisterState.Error(exception.message ?: "Error en el registro")
                    }
                )
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error en el registro")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Initial
    }

    companion object {
        fun provideFactory(
            veterinaryRepository: VeterinaryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(veterinaryRepository) as T
            }
        }
    }
}