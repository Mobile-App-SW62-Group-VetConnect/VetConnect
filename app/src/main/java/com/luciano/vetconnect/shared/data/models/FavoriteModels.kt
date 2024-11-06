package com.luciano.vetconnect.shared.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Favorite(
    val id: String,
    val userId: String,
    val veterinaryId: String,
    @SerializedName("createdAt")
    val createdAt: LocalDateTime? = null
)

data class FavoriteResponse(
    val favorites: List<Favorite> = emptyList()
)

data class FavoriteRequest(
    val userId: String,
    val veterinaryId: String
)

data class FavoriteVeterinaryDetails(
    val favorite: Favorite,
    val veterinary: Veterinary,
    val services: List<VeterinaryService>? = null
)