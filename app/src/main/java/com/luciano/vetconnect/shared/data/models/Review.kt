package com.luciano.vetconnect.shared.data.models

import com.google.gson.annotations.SerializedName

data class Review(
    val id: String,
    val veterinaryId: String,
    val userId: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    val comments: List<Comment> = emptyList()
)

data class Comment(
    val id: String,
    val reviewId: String,
    val userId: String,
    val userType: String,
    val userName: String,
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class ReviewResponse(
    val reviews: List<Review> = emptyList()
)