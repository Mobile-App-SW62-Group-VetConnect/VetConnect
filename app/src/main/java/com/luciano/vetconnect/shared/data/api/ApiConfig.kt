package com.luciano.vetconnect.shared.data.api

object ApiConfig {
    // Base URLs
    const val BASE_URL = "https://mocki.io/v1/"
    const val APIBASE_URL = "http://10.0.2.2:8080/"  // localhost for Android Emulator

    // Connection timeouts
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // Authentication endpoints
    const val SIGN_IN_URL = "api/v1/authentication/sign-in"
    const val SIGN_UP_URL = "api/v1/authentication/sign-up"
    const val CHANGE_PASSWORD_URL = "api/v1/authentication/{userId}/change-password"

    // User endpoints
    const val GET_ALL_USERS_URL = "api/v1/users"
    const val GET_USER_BY_ID_URL = "api/v1/users/{userId}"


    // Veterinary Center endpoints
    const val GET_ALL_VET_CENTERS_URL = "api/v1/vet-centers"
    const val GET_VET_CENTER_BY_ID_URL = "api/v1/vet-centers/{vetCenterId}"
    const val GET_VET_CENTER_BY_NAME_URL = "api/v1/vet-centers/name/{vetCenterName}"
    const val UPDATE_VET_CENTER_URL = "api/v1/vet-centers/{vetCenterId}"
    const val GET_VET_CENTER_IMAGES_URL = "api/v1/vet-centers/{vetCenterId}/images"
    const val ADD_VET_CENTER_IMAGE_URL = "api/v1/vet-centers/{vetCenterId}/images"

    // Pet Owner endpoints
    const val CREATE_PET_OWNER_URL = "api/v1/pet-owners"
    const val GET_ALL_PET_OWNERS_URL = "api/v1/pet-owners"
    const val GET_PET_OWNER_BY_ID_URL = "api/v1/pet-owners/{petOwnerId}"
    const val UPDATE_PET_OWNER_URL = "api/v1/pet-owners/{petOwnerId}"

    // Reviews endpoints
    const val CREATE_REVIEW_URL = "api/v1/reviews"
    const val GET_ALL_REVIEWS_URL = "api/v1/reviews"
    const val GET_REVIEWS_BY_VET_CENTER_URL = "api/v1/reviews/vet-center/{vetCenterId}"
    const val DELETE_REVIEW_URL = "api/v1/reviews"

    // Vet Services endpoints
    const val CREATE_VET_SERVICE_URL = "api/v1/vet-services"
    const val GET_ALL_VET_SERVICES_URL = "api/v1/vet-services"
    const val GET_VET_SERVICE_BY_ID_URL = "api/v1/vet-services/{vetServiceId}"
    const val GET_VET_SERVICES_BY_VET_CENTER_URL = "api/v1/vet-services/vet-center/{vetCenterId}"
    const val UPDATE_VET_SERVICE_URL = "api/v1/vet-services/{vetServiceId}"
    const val DELETE_VET_SERVICE_URL = "api/v1/vet-services/{vetServiceId}"

    // Favorites endpoints
    const val CREATE_FAVORITE_URL = "api/v1/favorites"
    const val GET_ALL_FAVORITES_URL = "api/v1/favorites"
    const val GET_FAVORITE_BY_ID_URL = "api/v1/favorites/by-id/{favoriteId}"
    const val GET_FAVORITES_BY_USER_URL = "api/v1/favorites/by-user/{userId}"
    const val DELETE_FAVORITE_URL = "api/v1/favorites/{favoriteId}"
}