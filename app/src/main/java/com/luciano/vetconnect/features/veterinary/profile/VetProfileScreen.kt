package com.luciano.vetconnect.features.veterinary.profile

import VetProfileViewModel
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.components.veterinary.BusinessHours
import com.luciano.vetconnect.shared.ui.components.veterinary.BusinessHoursItem
import com.luciano.vetconnect.shared.ui.components.veterinary.ProfileInfoItem
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetProfileScreen(
    navController: NavController,
    viewModel: VetProfileViewModel = viewModel(
        factory = VetProfileViewModel.provideFactory()
    )
) {
    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    // Manejo de efectos secundarios para navegación y mensajes
    LaunchedEffect(updateState) {
        when (updateState) {
            is UpdateProfileState.Success -> {
                // Mostrar mensaje de éxito si es necesario
            }
            is UpdateProfileState.Error -> {
                // Mostrar mensaje de error
            }
            else -> {}
        }
    }

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
        }
    ) { paddingValues ->
        when (val state = profileState) {
            is VetProfileState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrandColors.Primary)
                }
            }

            is VetProfileState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = SemanticColors.Error,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.loadVetProfile() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandColors.Primary
                            )
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is VetProfileState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Galería de imágenes
                    if (state.images.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.images) { image ->
                                AsyncImage(
                                    model = image.imageUrl,
                                    contentDescription = "Foto de la veterinaria",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Información del negocio
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = state.vetInfo.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextColors.Primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Información de contacto
                        state.vetInfo.contact?.let { contact ->
                            ProfileInfoItem(
                                icon = Icons.Outlined.Phone,
                                label = "Teléfono",
                                value = contact.phone
                            )

                            ProfileInfoItem(
                                icon = Icons.Outlined.Email,
                                label = "Email",
                                value = contact.email
                            )
                        }

                        ProfileInfoItem(
                            icon = Icons.Outlined.LocationOn,
                            label = "Dirección",
                            value = state.vetInfo.address
                        )

                        // Horarios de atención
                        if (state.vetInfo.businessHours.isNotEmpty()) {
                            Text(
                                text = "Horarios de atención",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextColors.Primary,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )

                            state.vetInfo.businessHours.forEach { hours ->
                                BusinessHoursItem(
                                    BusinessHours(
                                        dayRange = hours.days,
                                        startTime = hours.open,
                                        endTime = hours.close
                                    )
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                // No se espera que se llegue aquí
            }
        }
    }
}