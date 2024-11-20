package com.luciano.vetconnect.shared.data.models.backendmodels

import java.util.Date

data class ReviewResponse(
    val id: Long,
    val veterinaryId: Long,
    val userId: Long,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: Date,
    val updatedAt: Date
)

data class CreateReviewRequest(
    val vetCenterId: Long,
    val petOwnerId: Long,
    val rating: Int,
    val comments: String
)