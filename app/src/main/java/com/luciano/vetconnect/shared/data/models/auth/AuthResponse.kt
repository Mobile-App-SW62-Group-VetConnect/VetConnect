package com.luciano.vetconnect.shared.data.models.auth

// AuthResponse.kt
data class AuthResponse(
    val id: Long,
    val username: String,
    val token: String,
    val role: String
)