package com.luciano.vetconnect.shared.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.luciano.vetconnect.shared.data.models.backendmodels.AuthResponse
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {
    private const val PREFS_NAME = "VetConnectPrefs"
    private const val KEY_USER = "user_data"
    private const val KEY_TOKEN = "user_token"
    private const val KEY_VET_CENTER_ID = "vet_center_id"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    private val _currentUser = MutableStateFlow<AuthResponse?>(null)
    val currentUser: StateFlow<AuthResponse?> = _currentUser.asStateFlow()

    private val _currentVetCenter = MutableStateFlow<VetCenterResponse?>(null)
    val currentVetCenter: StateFlow<VetCenterResponse?> = _currentVetCenter.asStateFlow()

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
                clearUser()
            }
        }
    }

    fun setUser(user: AuthResponse) {
        _currentUser.value = user
        prefs.edit().apply {
            putString(KEY_USER, gson.toJson(user))
            putString(KEY_TOKEN, user.token)
            apply()
        }
    }

    fun setVetCenterInfo(vetCenter: VetCenterResponse) {
        _currentVetCenter.value = vetCenter
        prefs.edit().apply {
            putLong(KEY_VET_CENTER_ID, vetCenter.id)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null) ?: _currentUser.value?.token
    }

    fun getVetCenterId(): Long? {
        return _currentVetCenter.value?.id ?:
        if (prefs.contains(KEY_VET_CENTER_ID)) {
            prefs.getLong(KEY_VET_CENTER_ID, -1).takeIf { it != -1L }
        } else null
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
        _currentVetCenter.value = null
        prefs.edit().clear().apply()
    }
}