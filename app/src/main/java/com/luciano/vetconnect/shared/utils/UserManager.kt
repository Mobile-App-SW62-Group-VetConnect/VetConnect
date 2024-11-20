package com.luciano.vetconnect.shared.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.luciano.vetconnect.shared.data.models.backendmodels.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private const val PREFS_NAME = "VetConnectPrefs"
    private const val KEY_USER = "user_data"
    private const val KEY_TOKEN = "user_token"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    private val _currentUser = MutableStateFlow<AuthResponse?>(null)
    val currentUser: StateFlow<AuthResponse?> = _currentUser.asStateFlow()

    private var _isInitialized = false

    fun init(context: Context) {
        if (!_isInitialized) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadUserFromPrefs()
            _isInitialized = true
        }
    }

    private fun loadUserFromPrefs() {
        prefs.getString(KEY_USER, null)?.let { userJson ->
            try {
                val user = gson.fromJson(userJson, AuthResponse::class.java)
                _currentUser.value = user
            } catch (e: Exception) {
                // Si hay error al cargar el usuario, limpiamos los datos
                clearUser()
            }
        }
    }

    fun setUser(user: AuthResponse) {
        _currentUser.value = user
        // Guardamos en SharedPreferences
        prefs.edit().apply {
            putString(KEY_USER, gson.toJson(user))
            putString(KEY_TOKEN, user.token)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null) ?: _currentUser.value?.token
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null && getToken() != null
    }

    fun isVetUser(): Boolean {
        return _currentUser.value?.role == "VETERINARY"
    }

    fun getUserId(): Long? {
        return _currentUser.value?.id
    }

    fun clearUser() {
        _currentUser.value = null
        prefs.edit().clear().apply()
    }

    // Helper para obtener el token con el prefijo Bearer
    fun getBearerToken(): String? {
        return getToken()?.let { "Bearer $it" }
    }
}