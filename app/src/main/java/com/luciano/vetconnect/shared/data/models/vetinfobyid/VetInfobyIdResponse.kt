package com.luciano.vetconnect.shared.data.models.vetinfobyid

data class VetInfobyIdResponse(
    val id: Long,
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