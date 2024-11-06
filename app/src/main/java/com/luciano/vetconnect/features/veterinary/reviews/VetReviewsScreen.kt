package com.luciano.vetconnect.features.veterinary.reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.models.Review
import com.luciano.vetconnect.shared.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetReviewsScreen(
    navController: NavController,
    onMenuClick: () -> Unit,
    viewModel: VetReviewsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReviewsForVeterinary("vet-001")
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Stats Section
            ReviewStatsBox()

            when (val state = uiState) {
                is ReviewsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BrandColors.Primary)
                    }
                }
                is ReviewsUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = SemanticColors.Error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is ReviewsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.reviews) { review ->
                            ReviewListItem(
                                review = review,
                                onClick = {
                                    navController.navigate("review_detail/${review.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewListItem(
    review: Review,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundColors.Surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_avatar),
                    contentDescription = "Avatar",
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
                        fontWeight = FontWeight.Bold,
                        color = TextColors.Primary
                    )
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < review.rating)
                                    Icons.Filled.Star
                                else
                                    Icons.Filled.StarBorder,
                                contentDescription = null,
                                tint = BrandColors.Secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Text(
                    text = review.createdAt?.let { formatTimeAgo(it) } ?: "Fecha no disponible",
                    color = TextColors.Secondary,
                    fontSize = 12.sp
                )
            }

            Text(
                text = review.comment,
                color = TextColors.Primary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (review.comments.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = null,
                        tint = BrandColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${review.comments.size} respuesta(s)",
                        color = BrandColors.Primary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}



@Composable
private fun ReviewStatsBox() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundColors.Surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Calificación general",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "4.8",
                            style = MaterialTheme.typography.headlineLarge,
                            color = BrandColors.Primary
                        )
                        Row {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = if (index < 4) Icons.Filled.Star else Icons.Filled.StarHalf,
                                    contentDescription = null,
                                    tint = BrandColors.Secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total de reseñas",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary
                    )
                    Text(
                        text = "128",
                        style = MaterialTheme.typography.headlineLarge,
                        color = BrandColors.Primary
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimeAgo(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(dateString, formatter)
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