package com.luciano.vetconnect.shared.data.models.auth
// AuthRequest.kt
data class SignInRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val roles: List<String>,
    // Campos para CLIENT
    val name: String? = null,
    val dni: String? = null,
    val phone: String? = null,
    val address: String? = null,
    // Campos para VETERINARY
    val vetCenterRuc: String? = null,
    val vetCenterClinicName: String? = null,
    val vetCenterLicense: String? = null,
    val vetCenterAddress: String? = null,
    val vetCenterPhone: String? = null
)