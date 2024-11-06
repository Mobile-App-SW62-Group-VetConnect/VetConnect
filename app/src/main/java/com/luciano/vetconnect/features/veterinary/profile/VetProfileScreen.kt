package com.luciano.vetconnect.features.veterinary.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.components.BusinessHours
import com.luciano.vetconnect.shared.ui.components.BusinessHoursItem
import com.luciano.vetconnect.shared.ui.components.ProfileInfoItem
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetProfileScreen(
    navController: NavController
) {
    val photos = listOf(R.drawable.vet_clinic, R.drawable.vet_clinic, R.drawable.vet_clinic)
    val businessHours = listOf(
        BusinessHours("Lunes a Viernes", "09:00", "19:00"),
        BusinessHours("Sábados", "09:00", "14:00"),
        BusinessHours("Domingos y Feriados", "Cerrado", "Cerrado")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = TextColors.Primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BrandColors.Primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("vet_edit_profile") }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar perfil",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Galería de fotos
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photos) { photo ->
                    Image(
                        painter = painterResource(id = R.drawable.vet_clinic),
                        contentDescription = "Foto principal del local",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Información del negocio
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Clínica Veterinaria - El Roble",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextColors.Primary
                )

                Text(
                    text = "Miembro desde Octubre 2024",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColors.Secondary
                )
            }

            Divider(color = NeutralColors.Gray1)

            // Información de contacto
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Información del Negocio",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColors.Primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ProfileInfoItem(
                    icon = Icons.Outlined.Business,
                    label = "Número de RUC",
                    value = "20123456789"
                )

                ProfileInfoItem(
                    icon = Icons.Outlined.Email,
                    label = "Correo electrónico",
                    value = "contacto@elroble.com"
                )

                ProfileInfoItem(
                    icon = Icons.Outlined.Phone,
                    label = "Teléfono",
                    value = "01 234 5678"
                )

                ProfileInfoItem(
                    icon = Icons.Outlined.LocationOn,
                    label = "Dirección",
                    value = "Jr. Las Palmeras 123, Lima"
                )
            }

            Divider(color = NeutralColors.Gray1)

            // Horarios de atención
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Horarios de Atención",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColors.Primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                businessHours.forEach { hours ->
                    BusinessHoursItem(hours = hours)
                }
            }
        }
    }
}