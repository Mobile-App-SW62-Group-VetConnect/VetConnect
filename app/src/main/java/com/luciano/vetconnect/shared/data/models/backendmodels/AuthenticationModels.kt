package com.luciano.vetconnect.shared.data.models.backendmodels

// Request para iniciar sesión
data class SignInRequest(
    val email: String,
    val password: String
)

// Request para registrarse
data class SignUpRequest(
    val email: String,
    val password: String,
    val roles: List<String>,
    // Para CLIENT (PetOwner)
    val dni: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    // Para VETERINARY (VetCenter)
    val vetCenterRuc: String? = null,
    val vetCenterClinicName: String? = null,
    val vetCenterLicense: String? = null,
    val vetCenterAddress: String? = null,
    val vetCenterPhone: String? = null
)

// Response de autenticación
data class AuthResponse(
    val id: Long,
    val username: String,
    val token: String,
    val role: String
)

// Request para cambiar contraseña
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)