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
    val vetCenterRuc: String,
    val vetCenterClinicName: String,
    val vetCenterLicense: String,
    val vetCenterAddress: String,
    val vetCenterPhone: String
)