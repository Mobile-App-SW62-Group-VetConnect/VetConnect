package com.luciano.vetconnect.shared.data.models.backendmodels
import java.util.Date

data class FavoriteResponse(
    val id: Long,
    val userId: Long,
    val veterinaryId: Long,
    val createdAt: Date
)

data class CreateFavoriteRequest(
    val userId: Long,
    val veterinaryId: Long
)