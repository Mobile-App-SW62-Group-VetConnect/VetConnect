package com.luciano.vetconnect.shared.utils

import com.luciano.vetconnect.shared.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }

    fun getCurrentUserId(): String? {
        return _currentUser.value?.id
    }
}