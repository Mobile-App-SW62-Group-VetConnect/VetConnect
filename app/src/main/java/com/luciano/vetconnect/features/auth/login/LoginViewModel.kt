package com.luciano.vetconnect.features.auth.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.data.models.UserRole
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

class LoginViewModel : ViewModel() {
    private val repository = VeterinaryRepository.getInstance()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when (val result = repository.login(email, password)) {
                is ApiResult.Success -> {
                    // Guardar el usuario en el UserManager
                    UserManager.setCurrentUser(result.data.user)
                    val isVetUser = result.data.user.role == UserRole.VETERINARY
                    _loginState.value = LoginState.Success(isVetUser)
                }
                is ApiResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Initial
    }
}