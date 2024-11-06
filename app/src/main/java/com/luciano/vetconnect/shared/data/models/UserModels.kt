package com.luciano.vetconnect.shared.data.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String? = null,
    val imageUrl: String? = null,
    val role: UserRole,
    val veterinaryId: String? = null,
    val license: String? = null,
    val reviewCount: Int? = null,
    val favoriteCount: Int? = null,
    val createdAt: String,  // Mantenido como String
    val lastLoginAt: String? = null  // Mantenido como String
)

enum class UserRole {
    CLIENT,
    VETERINARY
}

data class Credentials(
    val email: String,
    val password: String,
    val userId: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class UserResponse(
    val users: List<User>,
    val credentials: CredentialsResponse
)

data class CredentialsResponse(
    val clients: List<Credentials>,
    val veterinaries: List<Credentials>
)