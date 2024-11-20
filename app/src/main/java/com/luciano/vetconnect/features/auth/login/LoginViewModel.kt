package com.luciano.vetconnect.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val isVetUser: Boolean) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val veterinaryRepository: VeterinaryRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String, isVetUser: Boolean) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = veterinaryRepository.signIn(email, password)

            result.fold(
                onSuccess = { authResponse ->
                    // Verificar que el rol coincida con lo seleccionado
                    val roleMatches = when (isVetUser) {
                        true -> authResponse.role == "VETERINARY"
                        false -> authResponse.role == "CLIENT"
                    }

                    if (roleMatches) {
                        UserManager.setUser(authResponse)
                        _loginState.value = LoginState.Success(isVetUser)
                    } else {
                        _loginState.value = LoginState.Error("Tipo de usuario incorrecto")
                    }
                },
                onFailure = { exception ->
                    _loginState.value = LoginState.Error(exception.message ?: "Error desconocido")
                }
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Initial
    }

    companion object {
        fun provideFactory(
            veterinaryRepository: VeterinaryRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(veterinaryRepository) as T
            }
        }
    }
}