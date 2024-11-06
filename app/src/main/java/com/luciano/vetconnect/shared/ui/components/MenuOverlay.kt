package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun MenuOverlay(
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_picture),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Carlos Chávez",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextColors.Primary
                            )
                            Text(
                                text = "Ver Perfil",
                                color = BrandColors.Primary,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { onNavigate("profile") }
                            )
                        }
                    }

                    Divider(color = NeutralColors.Gray1, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    MenuOption(icon = R.drawable.ic_home, text = "Inicio") { onNavigate("home") }
                    MenuOption(icon = R.drawable.ic_search, text = "Buscar") { onNavigate("search") }
                    MenuOption(icon = R.drawable.ic_saves, text = "Guardados") { onNavigate("favorites") }
                    MenuOption(icon = R.drawable.ic_notifications, text = "Notificaciones") { onNavigate("notifications") }
                    MenuOption(icon = R.drawable.ic_settings, text = "Ajustes") { onNavigate("settings") }
                }
            }
        }
    }
}

@Composable
private fun MenuOption(icon: Int, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = BrandColors.Primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = TextColors.Primary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}