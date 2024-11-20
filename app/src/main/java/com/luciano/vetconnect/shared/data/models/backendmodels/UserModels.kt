package com.luciano.vetconnect.shared.data.models.backendmodels

data class UserResponse(
    val id: Long,
    val username: String,
    val roles: List<String>
)

