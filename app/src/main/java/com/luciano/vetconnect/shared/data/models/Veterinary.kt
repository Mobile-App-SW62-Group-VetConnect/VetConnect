package com.luciano.vetconnect.shared.data.models

data class Veterinary(
    val id: String,
    val name: String = "",
    val address: String = "",
    val coordinates: Coordinates? = null,
    val rating: Int = 0,
    val totalReviews: Int = 0,
    val contact: Contact? = null,
    val businessHours: List<BusinessHours> = emptyList(),
    val features: List<String> = emptyList(),
    val serviceIds: List<String> = emptyList(),
    val reviews: List<Review>? = null,
    val images: List<VeterinaryImage>? = null
) {
    data class Contact(
        val phone: String? = null,
        val email: String? = null,
        val website: String? = null
    )

    data class Coordinates(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )

    data class BusinessHours(
        val days: String = "",
        val open: String = "",
        val close: String = ""
    )

    data class VeterinaryImage(
        val id: String = "",
        val url: String = "",
        val type: String = ""
    )
}

data class VeterinaryResponse(
    val veterinaries: List<Veterinary> = emptyList()
)