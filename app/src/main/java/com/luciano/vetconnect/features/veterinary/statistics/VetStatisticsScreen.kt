package com.luciano.vetconnect.features.veterinary.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetStatisticsScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary
            )

            // Cards de métricas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Visitas al perfil",
                    value = "128",
                    subtitle = "Últimas 24 horas"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Calificación promedio",
                    value = "4.8",
                    subtitle = "233 reseñas",
                    showStars = true
                )
            }

            // Gráfica de visitas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Visitas al perfil",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            // Distribución de calificaciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Distribución de calificaciones",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RatingDistribution()
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    showStars: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = TextColors.Secondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = TextColors.Primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            if (showStars) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        val rating = value.toFloatOrNull() ?: 0f
                        Icon(
                            imageVector = when {
                                index + 1 <= rating.toInt() -> Icons.Default.Star
                                index + 0.5f <= rating -> Icons.Default.StarHalf
                                else -> Icons.Default.StarOutline
                            },
                            contentDescription = null,
                            tint = BrandColors.Secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = TextColors.Secondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun LineGraph(
    modifier: Modifier = Modifier
) {
    val points = listOf(65f, 85f, 72f, 90f, 95f, 88f)
    val maxValue = points.maxOrNull() ?: 0f

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spaceBetweenPoints = width / (points.size - 1)

        val path = Path()
        points.forEachIndexed { index, point ->
            val x = index * spaceBetweenPoints
            val y = height - (point / maxValue * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // Dibuja los puntos
            drawCircle(
                color = BrandColors.Primary,
                radius = 8f,
                center = Offset(x, y)
            )
        }

        // Dibuja la línea
        drawPath(
            path = path,
            color = BrandColors.Primary,
            style = Stroke(
                width = 4f,
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
private fun RatingDistribution() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingBar(stars = 5, percentage = 45f)
        RatingBar(stars = 4, percentage = 35f)
        RatingBar(stars = 3, percentage = 15f)
        RatingBar(stars = 2, percentage = 3f)
        RatingBar(stars = 1, percentage = 2f)
    }
}

@Composable
private fun RatingBar(
    stars: Int,
    percentage: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$stars★",
            color = BrandColors.Secondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${percentage.toInt()}%",
            color = TextColors.Secondary,
            fontSize = 12.sp
        )
    }
}