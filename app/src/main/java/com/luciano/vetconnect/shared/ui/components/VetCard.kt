package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.data.models.Veterinary
import com.luciano.vetconnect.shared.data.models.VeterinaryService
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun VetCard(
    veterinary: Veterinary,
    services: List<VeterinaryService>,
    navController: NavController,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate(Screen.VetDetail.createRoute(veterinary.id))
            },
        colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con nombre, rating y botón de favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = veterinary.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextColors.Primary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RatingStars(rating = veterinary.rating)
                        Text(
                            text = "(${veterinary.totalReviews})",
                            color = TextColors.Secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                ) {
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

            // Imagen principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vet_clinic),
                    contentDescription = "Imagen de ${veterinary.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                ) {
                    veterinary.features.take(2).forEach { feature ->
                        FeatureBadge(feature)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            // Dirección y horario
            Column(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BrandColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = veterinary.address,
                        color = TextColors.Secondary,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = BrandColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = getBusinessHoursText(veterinary),
                        color = TextColors.Secondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Sección de rango de precios
            PriceRangeSection(services)
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
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun PriceRangeSection(services: List<VeterinaryService>) {
    if (services.isNotEmpty()) {
        val minPrice = services.minOf { it.price }
        val maxPrice = services.maxOf { it.price }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(
                color = NeutralColors.Gray1,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PriceCheck,
                    contentDescription = null,
                    tint = BrandColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Rango de precios:",
                    color = TextColors.Secondary,
                    fontSize = 14.sp
                )
            }

            Text(
                text = "S/ ${minPrice.toInt()} - S/ ${maxPrice.toInt()}",
                color = BrandColors.Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun getBusinessHoursText(veterinary: Veterinary): String {
    return if (veterinary.features.contains("24/7")) {
        "Abierto 24/7"
    } else {
        veterinary.businessHours.firstOrNull()?.let { hours ->
            if (hours.open != "Cerrado") "Hoy: ${hours.open} - ${hours.close}" else "Cerrado"
        } ?: "Horario no disponible"
    }
}