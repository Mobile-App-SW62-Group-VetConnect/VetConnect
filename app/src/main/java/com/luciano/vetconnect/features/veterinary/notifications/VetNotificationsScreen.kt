package com.luciano.vetconnect.features.veterinary.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NotificationItem(
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,
    val isRead: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetNotificationsScreen(
    navController: NavController
) {
    var notifications by remember { mutableStateOf(listOf(
        NotificationItem(
            "Nueva reseña",
            "Juan Pérez ha dejado una reseña en tu perfil",
            LocalDateTime.now().minusHours(2)
        ),
        NotificationItem(
            "Nueva reseña",
            "María García ha dejado una reseña en tu perfil",
            LocalDateTime.now().minusDays(1)
        ),
        NotificationItem(
            "Nueva calificación",
            "Tu calificación promedio ha aumentado a 4.5 estrellas",
            LocalDateTime.now().minusDays(2)
        )
    )) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", color = TextColors.Primary) },
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
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = TextColors.Secondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay notificaciones",
                        color = TextColors.Secondary,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                BackgroundColors.Surface
            else
                BrandColors.Primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = notification.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColors.Primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.description,
                color = TextColors.Secondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatDateTime(notification.dateTime),
                color = TextColors.Tertiary,
                fontSize = 12.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        dateTime.toLocalDate() == now.toLocalDate() -> {
            "Hoy ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
        dateTime.toLocalDate() == now.minusDays(1).toLocalDate() -> {
            "Ayer ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
        else -> {
            dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }
    }
}