package com.luciano.vetconnect.shared.data.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String?,
    val imageUrl: String?,
    val role: UserRole, // Enum en lugar de String
    val veterinaryId: String?,
    val license: String?,
    val reviewCount: Int?,
    val favoriteCount: Int?,
    val createdAt: String,
    val lastLoginAt: String?
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