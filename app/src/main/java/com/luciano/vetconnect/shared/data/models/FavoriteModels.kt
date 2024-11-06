package com.luciano.vetconnect.shared.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Favorite(
    val id: String,
    val userId: String,
    val veterinaryId: String,
    val createdAt: String
)

data class FavoritesResponse(
    val favorites: List<Favorite>
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