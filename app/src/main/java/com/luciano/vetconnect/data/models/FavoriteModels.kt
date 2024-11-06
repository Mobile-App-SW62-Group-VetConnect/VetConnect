data class Favorite(
    val id: String = "",
    val userId: String,
    val veterinaryId: String,
    val createdAt: String = System.currentTimeMillis().toString()
)

// Respuesta de la API para favoritos
data class FavoriteResponse(
    val success: Boolean,
    val message: String,
    val data: List<Favorite>
)

// Request para crear un favorito
data class CreateFavoriteRequest(
    val userId: String,
    val veterinaryId: String
)

// Request para eliminar un favorito
data class DeleteFavoriteRequest(
    val userId: String,
    val veterinaryId: String
) 