package com.luciano.vetconnect.features.veterinary.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.models.ServiceOnlyVets
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetServicesScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    var services by remember { mutableStateOf(sampleServices) }
    var showDeleteDialog by remember { mutableStateOf<ServiceOnlyVets?>(null) }

    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar servicio", color = TextColors.Primary) },
            text = { Text("¿Estás seguro de que deseas eliminar ${showDeleteDialog?.name}?",
                color = TextColors.Primary) },
            confirmButton = {
                Button(
                    onClick = {
                        services = services.filter { it != showDeleteDialog }
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SemanticColors.Error,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar", color = TextColors.Tertiary)
                }
            },
            containerColor = BackgroundColors.Surface
        )
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_service") },
                containerColor = BrandColors.Primary,
                contentColor = TextColors.OnDark
            ) {
                Icon(Icons.Default.Add, "Agregar servicio")
            }
        },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Servicios",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    ServiceCard(
                        service = service,
                        onEditClick = { navController.navigate("edit_service/${service.id}") },
                        onDeleteClick = { showDeleteDialog = service }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceCard(
    service: ServiceOnlyVets,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColors.Primary
                )
                Text(
                    text = "S/ ${service.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.Primary
                )
            }

            Text(
                text = service.description,
                color = TextColors.Secondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, null, tint = BrandColors.Primary)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar", color = BrandColors.Primary)
                }
                TextButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, null, tint = SemanticColors.Error)
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar", color = SemanticColors.Error)
                }
            }
        }
    }
}


val sampleServices = listOf(
    ServiceOnlyVets(
        "1",
        "Consulta general",
        "Evaluación completa del estado de salud de tu mascota",
        60.0,
        30
    ),
    ServiceOnlyVets(
        "2",
        "Vacunación",
        "Aplicación de vacunas según el calendario de tu mascota",
        80.0,
        20
    ),
    ServiceOnlyVets(
        "3",
        "Baño y peluquería",
        "Servicio completo de higiene y estética para tu mascota",
        45.0,
        90
    )
)

