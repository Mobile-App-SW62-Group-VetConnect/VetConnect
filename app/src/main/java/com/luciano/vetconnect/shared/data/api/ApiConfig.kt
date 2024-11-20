package com.luciano.vetconnect.shared.data.api

object ApiConfig {
    //MOCK API
    const val BASE_URL = "https://mocki.io/v1/"
    const val VETERINARIES_ENDPOINT = "https://mocki.io/v1/bd5e139a-4409-476e-b15e-fa67be717a7e"
    const val SERVICES_ENDPOINT = "https://mocki.io/v1/b66e9954-1966-490f-9168-6556b5ece9ea"
    const val REVIEWS_ENDPOINT = "https://mocki.io/v1/c3b75cf5-064a-4fb0-942a-c5508b1858c3"  // Reemplaza con tu endpoint
    const val USERS_ENDPOINT = "https://mocki.io/v1/6eec656c-acb4-4f28-a927-fe8111d2acd7"
    const val FAVORITES_ENDPOINT = "https://mocki.io/v1/646ff380-c8ce-4123-83fc-8a9331bfe01e"


    //BACKEND API
    const val APIBASE_URL = "http://10.0.2.2:8080/"  // localhost para Android Emulator

    // Authentication endpoints
    const val SIGN_IN_URL = "api/v1/authentication/sign-in"
    const val SIGN_UP_URL = "api/v1/authentication/sign-up"

    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}