package com.luciano.vetconnect.shared.data.models

enum class SortOption(val displayName: String) {
    NAME_ASC("Nombre (A-Z)"),
    NAME_DESC("Nombre (Z-A)"),
    RATING_HIGH("Mayor calificación"),
    RATING_LOW("Menor calificación"),
    PRICE_LOW("Menor precio"),
    PRICE_HIGH("Mayor precio")
}

data class FilterOptions(
    val minRating: Int = 0,
    val maxPrice: Int = Int.MAX_VALUE,
    val selectedServices: Set<String> = emptySet()
)