package com.luciano.vetconnect.shared.utils

import com.luciano.vetconnect.shared.data.models.User
import com.luciano.vetconnect.shared.data.models.auth.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private val _currentUser = MutableStateFlow<AuthResponse?>(null)
    val currentUser: StateFlow<AuthResponse?> = _currentUser.asStateFlow()

    fun setUser(user: AuthResponse) {
        _currentUser.value = user
    }

    fun getToken(): String? {
        return _currentUser.value?.token
    }

    fun clearUser() {
        _currentUser.value = null
    }
}