package com.luciano.vetconnect.features.veterinary.reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.data.models.Review
import com.luciano.vetconnect.shared.ui.components.RatingStars
import com.luciano.vetconnect.shared.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetReviewDetailScreen(
    navController: NavController,
    reviewId: String,
    viewModel: VetReviewsViewModel = viewModel()
) {
    val selectedReview by viewModel.selectedReview.collectAsState()
    var showReplyDialog by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }

    LaunchedEffect(reviewId) {
        viewModel.loadReviewById(reviewId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedReview()
        }
    }

    if (showReplyDialog) {
        AlertDialog(
            onDismissRequest = { showReplyDialog = false },
            title = { Text("Responder reseña", color = TextColors.Primary) },
            text = {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tu respuesta", color = TextColors.Primary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReplyDialog = false
                        replyText = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReplyDialog = false }) {
                    Text("Cancelar", color = TextColors.Tertiary)
                }
            },
            containerColor = BackgroundColors.Surface
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de reseña", color = TextColors.Primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BrandColors.Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColors.Primary
                )
            )
        },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        if (selectedReview == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandColors.Primary)
            }
        } else {
            ReviewContent(
                review = selectedReview!!,
                showReplyDialog = showReplyDialog,
                onShowReplyDialog = { showReplyDialog = it },
                paddingValues = paddingValues
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ReviewContent(
    review: Review,
    showReplyDialog: Boolean,
    onShowReplyDialog: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Card original
        Card(
            colors = CardDefaults.cardColors(
                containerColor = BackgroundColors.Surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user_avatar),
                        contentDescription = "Avatar de ${review.userName}",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = review.userName,
                            fontWeight = FontWeight.Bold,
                            color = TextColors.Primary
                        )
                        RatingStars(rating = review.rating)
                    }
                }

                Text(
                    text = review.comment,
                    color = TextColors.Primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = review.createdAt?.let { formatTimeAgo(it) } ?: "Fecha no disponible",
                    color = TextColors.Secondary,
                    fontSize = 12.sp
                )
            }
        }

        // Comentarios
        review.comments.forEach { comment ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NeutralColors.Gray1.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (comment.userType == "VETERINARY")
                                    R.drawable.vet_connect_logo
                                else
                                    R.drawable.user_avatar
                            ),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = comment.userName,
                                    fontWeight = FontWeight.Medium,
                                    color = TextColors.Primary
                                )
                                if (comment.userType == "VETERINARY") {
                                    Surface(
                                        color = BrandColors.Primary.copy(alpha = 0.1f),
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = "Veterinaria",
                                            color = BrandColors.Primary,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }
                                }
                            }
                            Text(
                                text = comment.createdAt?.let { formatTimeAgo(it) } ?: "Fecha no disponible",
                                color = TextColors.Secondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Text(
                        text = comment.content,
                        color = TextColors.Primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de respuesta (solo visible para la veterinaria)
        if (review.veterinaryId == "vet-001") { // TODO: Cambiar por verificación real de rol
            OutlinedButton(
                onClick = { onShowReplyDialog(true) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BrandColors.Primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Reply,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (review.comments.any { it.userType == "VETERINARY" })
                        "Editar respuesta"
                    else
                        "Responder reseña"
                )
            }
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

