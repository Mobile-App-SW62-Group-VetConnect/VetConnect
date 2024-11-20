package com.luciano.vetconnect.shared.data.models.backendmodels

data class VetCenterResponse(
    val id: Long,
    val user_id: Long,
    val name: String,
    val address: String,
    val imageProfile: String,
    val description: String,
    val businessHours: List<BusinessHour>,
    val contact: Contact
)

data class BusinessHour(
    val days: String,
    val open: String,
    val close: String
)

data class Contact(
    val phone: String,
    val email: String
)

data class UpdateVetCenterRequest(
    val name: String?,
    val email: String?,
    val ruc: String?,
    val phone: String?,
    val imageProfile: String?,
    val description: String?,
    val address: String?,
    val businessHours: List<BusinessHour>?
)

data class VetCenterImageResponse(
    val vetCenterImageId: Long,
    val imageUrl: String
)

data class AddVetCenterImageRequest(
    val imageUrl: String
)