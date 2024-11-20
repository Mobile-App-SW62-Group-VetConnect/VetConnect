package com.luciano.vetconnect.shared.data.models.backendmodels

data class PetOwnerResponse(
    val id: Long,
    val user_id: Long,
    val name: String,
    val email: String,
    val dni: String,
    val phone: String,
    val photo: String
)

data class CreatePetOwnerRequest(
    val name: String,
    val email: String,
    val dni: String,
    val phone: String,
    val photo: String
)

data class UpdatePetOwnerRequest(
    val name: String?,
    val email: String?,
    val dni: String?,
    val phone: String?,
    val photo: String?
)