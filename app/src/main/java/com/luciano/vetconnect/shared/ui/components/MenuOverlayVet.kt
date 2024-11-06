package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun MenuOverlayVet(
    isOpen: Boolean,
    onClose: () -> Unit,
    onNavigate: (String) -> Unit
) {
    if (isOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralColors.Black.copy(alpha = 0.5f))
                .clickable(onClick = onClose)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(BackgroundColors.Primary)
                    .clickable(onClick = {})
                    .padding(16.dp)
            ) {
                Column {
                    // Header con información de la veterinaria
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.vet_clinic),
                            contentDescription = "Foto de la veterinaria",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Veterinaria El Roble",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextColors.Primary
                            )
                            Text(
                                text = "Ver Perfil",
                                color = BrandColors.Primary,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { onNavigate(Screen.VetProfile.route) }
                            )
                        }
                    }

                    Divider(color = NeutralColors.Gray1, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Opciones simplificadas para veterinarias
                    MenuOption(
                        icon = Icons.Outlined.Home,
                        text = "Inicio"
                    ) { onNavigate(Screen.HomeVet.route) }

                    MenuOption(
                        icon = Icons.Outlined.Star,
                        text = "Reseñas"
                    ) { onNavigate(Screen.VetReviews.route) }

                    MenuOption(
                        icon = Icons.Outlined.Pets,
                        text = "Servicios"
                    ) { onNavigate(Screen.VetServices.route) }

                    MenuOption(
                        icon = Icons.Outlined.InsertChart,
                        text = "Estadísticas"
                    ) { onNavigate(Screen.VetStatistics.route) }

                    MenuOption(
                        icon = Icons.Outlined.Notifications,
                        text = "Notificaciones"
                    ) { onNavigate(Screen.VetNotifications.route) }

                    MenuOption(
                        icon = Icons.Outlined.Settings,
                        text = "Ajustes"
                    ) { onNavigate(Screen.VetSettings.route) }

                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        }
    }
}

@Composable
private fun MenuOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    textColor: androidx.compose.ui.graphics.Color = TextColors.Primary,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = BrandColors.Primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
