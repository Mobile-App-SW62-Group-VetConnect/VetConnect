package com.luciano.vetconnect.shared.data.models

data class VeterinaryWithDetails(
    val veterinary: Veterinary,
    val services: List<VeterinaryService>,
    val reviews: List<Review>
)