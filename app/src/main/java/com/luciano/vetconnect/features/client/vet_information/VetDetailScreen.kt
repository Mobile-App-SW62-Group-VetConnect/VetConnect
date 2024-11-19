package com.luciano.vetconnect.features.client.vet_information

import android.os.Build
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.models.*
import com.luciano.vetconnect.shared.ui.components.RatingStars
import com.luciano.vetconnect.shared.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetDetailScreen(
    navController: NavController,
    veterinaryId: String,
    onMenuClick: () -> Unit,
    viewModel: VetDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(veterinaryId) {
        viewModel.loadVeterinaryDetails(veterinaryId)
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary,
        floatingActionButton = {
            when (val state = uiState) {
                is VetDetailUiState.Success -> {
                    state.data?.let { veterinaryWithDetails ->
                        ReviewButton(
                            veterinary = veterinaryWithDetails.veterinary,
                            onReviewClick = { /* Implementar cuando tengamos el backend */ }
                        )
                    }
                }
                else -> { /* No mostrar el botón si no hay datos */ }
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when (val state = uiState) {
                is VetDetailUiState.Loading -> LoadingState(paddingValues)
                is VetDetailUiState.Error -> ErrorState(state.message, paddingValues)
                is VetDetailUiState.Success -> {
                    state.data?.let { veterinaryWithDetails ->
                        VetDetailContent(
                            veterinaryWithDetails = veterinaryWithDetails,
                            isFavorite = state.isFavorite,
                            paddingValues = paddingValues,
                            onFavoriteClick = { viewModel.toggleFavorite() }
                        )
                    } ?: ErrorState("No se encontró la información de la veterinaria", paddingValues)
                }
                else -> ErrorState("Estado no válido", paddingValues)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ReviewButton(
    veterinary: Veterinary,
    onReviewClick: () -> Unit
) {
    var showReviewDialog by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    // Botón flotante para dejar reseña
    FloatingActionButton(
        onClick = { showReviewDialog = true },
        modifier = Modifier.padding(16.dp),
        containerColor = BrandColors.Primary,
        contentColor = TextColors.OnDark
    ) {
        Icon(
            imageVector = Icons.Default.RateReview,
            contentDescription = "Dejar reseña"
        )
    }

    // Diálogo para escribir la reseña
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Dejar una reseña", color = TextColors.Primary) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Calificación",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColors.Primary
                    )

                    // Estrellas seleccionables
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < rating)
                                    Icons.Filled.Star
                                else
                                    Icons.Filled.StarOutline,
                                contentDescription = "Estrella ${index + 1}",
                                tint = BrandColors.Secondary,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { rating = index + 1 }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Tu opinión", color = TextColors.Primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        ),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Aquí iría la lógica para enviar la reseña
                        // Por ahora solo cerramos el diálogo
                        showReviewDialog = false
                        rating = 0
                        comment = ""
                    },
                    enabled = rating > 0 && comment.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Enviar reseña")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showReviewDialog = false
                        rating = 0
                        comment = ""
                    }
                ) {
                    Text("Cancelar", color = TextColors.Tertiary)
                }
            },
            containerColor = BackgroundColors.Surface
        )
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = BrandColors.Primary)
    }
}

@Composable
private fun ErrorState(message: String, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = SemanticColors.Error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = SemanticColors.Error,
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun VetDetailContent(
    veterinaryWithDetails: VeterinaryWithDetails,
    isFavorite: Boolean,
    paddingValues: PaddingValues,
    onFavoriteClick: () -> Unit
) {
    val veterinary = veterinaryWithDetails.veterinary
    val services = veterinaryWithDetails.services
    val reviews = veterinaryWithDetails.reviews

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item { HeaderSection(veterinary) }
        item { InfoSection(veterinary, isFavorite, onFavoriteClick) }
        item { ActionButtonsSection() }
        item { BusinessHoursSection(veterinary.businessHours) }
        item { ServicesSection(services) }
        item { ReviewsSection(reviews) }
    }
}
@Composable
private fun HeaderSection(veterinary: Veterinary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vet_clinic),
            contentDescription = "Foto de ${veterinary.name}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            veterinary.features?.let { features ->
                features.take(2).forEach { feature ->
                    FeatureBadge(feature)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureBadge(text: String) {
    Surface(
        color = BrandColors.Primary.copy(alpha = 0.9f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoSection(
    veterinary: Veterinary,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = veterinary.name.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextColors.Primary,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    RatingStars(rating = veterinary.rating)
                    Text(
                        text = "(${veterinary.totalReviews} reseñas)",
                        color = TextColors.Secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Filled.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite)
                        "Quitar de favoritos"
                    else
                        "Agregar a favoritos",
                    tint = if (isFavorite)
                        BrandColors.Primary
                    else
                        TextColors.Secondary
                )
            }
        }

        veterinary.contact?.let { contact ->
            contact.phone?.takeIf { it.isNotBlank() }?.let { phone ->
                ContactInfoItem(
                    icon = Icons.Default.Phone,
                    text = phone
                )
            }

            contact.website?.takeIf { it.isNotBlank() }?.let { website ->
                ContactInfoItem(
                    icon = Icons.Default.Language,
                    text = website
                )
            }
        }

        if (!veterinary.address.isNullOrBlank()) {
            ContactInfoItem(
                icon = Icons.Default.LocationOn,
                text = veterinary.address
            )
        }
    }
}

@Composable
private fun ContactInfoItem(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.Primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            color = TextColors.Secondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun ActionButtonsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(
            icon = Icons.Default.Share,
            text = "Compartir"
        )
        ActionButton(
            icon = Icons.Default.Phone,
            text = "Llamar"
        )
        ActionButton(
            icon = Icons.Default.LocationOn,
            text = "Ubicación"
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BrandColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextColors.Primary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun BusinessHoursSection(businessHours: List<Veterinary.BusinessHours>?) {
    businessHours?.takeIf { it.isNotEmpty() }?.let { hours ->
        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Horarios de atención",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextColors.Primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                hours.forEach { hours ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hours.days,
                            color = TextColors.Primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )

                        if (hours.open != "Cerrado") {
                            Text(
                                text = "${hours.open} - ${hours.close}",
                                color = TextColors.Secondary
                            )
                        } else {
                            Text(
                                text = "Cerrado",
                                color = TextColors.Secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesSection(services: List<VeterinaryService>) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Servicios",
                style = MaterialTheme.typography.titleMedium,
                color = TextColors.Primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            services.filter { it.isActive }.forEach { service ->
                ServiceItem(service)
                if (service != services.last()) {
                    Divider(
                        color = NeutralColors.Gray1,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ServiceItem(service: VeterinaryService) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = service.name,
                color = TextColors.Primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "S/ ${String.format("%.2f", service.price)}",
                color = BrandColors.Primary,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = service.description,
            color = TextColors.Secondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (!service.features.isNullOrEmpty()) {
            FlowRow(
                modifier = Modifier.padding(top = 8.dp),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                crossAxisAlignment = FlowCrossAxisAlignment.Start
            ) {
                service.features.forEach { feature ->
                    ServiceFeatureChip(feature)
                }
            }
        }

        if (service.duration != null) {
            Text(
                text = "Duración: ${service.duration} min",
                color = TextColors.Secondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ServiceFeatureChip(text: String) {
    Surface(
        color = BrandColors.Primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = BrandColors.Primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ReviewsSection(reviews: List<Review>?) {
    if (!reviews.isNullOrEmpty()) {
        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reseñas",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (reviews.size > 2) {
                        TextButton(onClick = { /* Navigate to all reviews */ }) {
                            Text(
                                text = "Ver todas",
                                color = BrandColors.Primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                reviews.take(2).forEach { review ->
                    ReviewItem(review)
                    if (review != reviews[1]) {
                        Divider(
                            color = NeutralColors.Gray1,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ReviewItem(review: Review) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.user_avatar),
                contentDescription = "Avatar de ${review.userName}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = review.userName,
                    fontWeight = FontWeight.Medium,
                    color = TextColors.Primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(rating = review.rating)
                    Text(
                        // Modificamos esta parte para manejar el LocalDateTime nullable
                        text = review.createdAt?.let { formatTimeAgo(it) } ?: "Fecha no disponible",
                        color = TextColors.Secondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Text(
            text = review.comment,
            color = TextColors.Primary,
            fontSize = 14.sp,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = if (review.comments.isNotEmpty()) 8.dp else 0.dp
            )
        )

        review.comments.forEach { comment ->
            CommentItem(comment)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = NeutralColors.Gray1.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.userName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextColors.Primary
                )
                Text(
                    text = if (comment.userType == "VETERINARY") " (Veterinaria)" else "",
                    fontSize = 12.sp,
                    color = TextColors.Secondary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(
                text = comment.content,
                color = TextColors.Primary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = comment.createdAt?.let { formatTimeAgo(it) } ?: "Fecha no disponible",
                color = TextColors.Secondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimeAgo(dateTimeString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val now = LocalDateTime.now()
        val days = ChronoUnit.DAYS.between(dateTime, now)

        when {
            days == 0L -> "Hoy"
            days == 1L -> "Ayer"
            days < 7L -> "Hace $days días"
            days < 30L -> "Hace ${days / 7} semanas"
            days < 365L -> "Hace ${days / 30} meses"
            else -> "Hace ${days / 365} años"
        }
    } catch (e: Exception) {
        "Fecha no disponible"
    }
}
