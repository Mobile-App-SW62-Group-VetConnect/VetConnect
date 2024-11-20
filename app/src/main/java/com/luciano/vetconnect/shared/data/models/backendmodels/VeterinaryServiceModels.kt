package com.luciano.vetconnect.shared.data.models.vetservices

data class VetServiceResponse(
    val id: Long,
    val veterinaryId: Long,
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
    val category: ServiceCategory,
    val features: List<String>,
    val isActive: Boolean
)

data class CreateVetServiceRequest(
    val vetId: Long,
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
    val category: ServiceCategory,
    val features: List<String>,
    val isActive: Boolean
)

data class UpdateVetServiceRequest(
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
    val category: ServiceCategory,
    val features: List<String>,
    val isActive: Boolean
)

enum class ServiceCategory {
    Consultas,
    Grooming,
    Prevencion,
    Emergencias,
    Laboratorio,
    Cirugia,
    Diagnostico,
    Hospedaje,
    Especialidades,
    Dental,
    Rehabilitacion
}