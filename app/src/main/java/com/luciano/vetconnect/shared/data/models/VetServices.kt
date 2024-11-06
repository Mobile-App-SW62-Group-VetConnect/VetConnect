package com.luciano.vetconnect.shared.data.models

data class VeterinaryService(
    val id: String,
    val veterinaryId: String,
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int?,
    val category: String,
    val features: List<String>,
    val isActive: Boolean
)

data class ServiceResponse(
    val services: List<VeterinaryService>
)

data class VeterinaryWithServices(
    val veterinary: Veterinary,
    val services: List<VeterinaryService>
)