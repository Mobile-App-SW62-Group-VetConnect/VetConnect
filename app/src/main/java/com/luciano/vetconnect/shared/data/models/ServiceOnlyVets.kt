package com.luciano.vetconnect.shared.data.models

data class ServiceOnlyVets(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int? = null, // duración en minutos
    val veterinaryId: String? = null, // referencia a la veterinaria que ofrece el servicio
    val isActive: Boolean = true,
    val imageUrl: String? = null
)

data class ServiceResponse2(
    val services: List<ServiceOnlyVets>
)