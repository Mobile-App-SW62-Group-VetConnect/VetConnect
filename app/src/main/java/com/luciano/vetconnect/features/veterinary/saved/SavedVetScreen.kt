package com.luciano.vetconnect.features.veterinary.saved

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.data.models.Veterinary
import com.luciano.vetconnect.shared.data.models.VeterinaryService
import com.luciano.vetconnect.shared.data.models.Favorite
import com.luciano.vetconnect.shared.ui.theme.*
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.api.ApiResult
import com.luciano.vetconnect.shared.ui.components.VetCard
import com.luciano.vetconnect.shared.ui.components.FilterButtons
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository

@Composable
fun SavedVetScreen(navController: NavController, onMenuClick: () -> Unit) {
    val context = LocalContext.current
    var veterinaries by remember { mutableStateOf<List<Veterinary>>(emptyList()) }
    var services by remember { mutableStateOf<Map<String, List<VeterinaryService>>>(emptyMap()) }
    var favorites by remember { mutableStateOf<List<Favorite>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val repository = remember { VeterinaryRepository.getInstance() }
    
    LaunchedEffect(key1 = true) {
        try {
            val userId = "usr-001"
            
            when (val favoritesResult = repository.getFavoritesForUser(userId)) {
                is ApiResult.Success -> {
                    favorites = favoritesResult.data
                    Log.d("SavedVetScreen", "Favoritos obtenidos: ${favorites.size}")
                    
                    when (val veterinariesResult = repository.getVeterinaries()) {
                        is ApiResult.Success -> {
                            // Filtramos las veterinarias que están en favoritos
                            veterinaries = veterinariesResult.data.filter { vet ->
                                favorites.any { fav -> fav.veterinaryId == vet.id }
                            }
                            Log.d("SavedVetScreen", "Veterinarias filtradas: ${veterinaries.size}")
                            
                            // Obtenemos los servicios para cada veterinaria
                            val servicesMap = mutableMapOf<String, List<VeterinaryService>>()
                            veterinaries.forEach { vet ->
                                when (val servicesResult = repository.getServicesForVeterinary(vet.id)) {
                                    is ApiResult.Success -> {
                                        servicesMap[vet.id] = servicesResult.data
                                    }
                                    is ApiResult.Error -> {
                                        Log.e("SavedVetScreen", "Error al obtener servicios: ${servicesResult.message}")
                                        servicesMap[vet.id] = emptyList()
                                    }
                                }
                            }
                            services = servicesMap
                            isLoading = false
                        }
                        is ApiResult.Error -> {
                            error = veterinariesResult.message
                            isLoading = false
                        }
                    }
                }
                is ApiResult.Error -> {
                    error = favoritesResult.message
                    isLoading = false
                }
            }
        } catch (e: Exception) {
            error = e.message ?: "Error desconocido"
            isLoading = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BrandColors.Primary
                    )
                }
                error != null -> {
                    Text(
                        text = error!!,
                        color = SemanticColors.Error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                veterinaries.isEmpty() -> {
                    Text(
                        text = "No tienes veterinarias guardadas",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = TextColors.Primary
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        FilterButtons()

                        Text(
                            text = "Veterinarias guardadas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextColors.Primary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(veterinaries) { veterinary ->
                                VetCard(
                                    veterinary = veterinary,
                                    services = services[veterinary.id] ?: emptyList(),
                                    navController = navController,
                                    isFavorite = true,
                                    onFavoriteClick = { /* Se implementará cuando tengamos backend real */ }
                                )
                            }
                            item { 
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = true,
            onClick = { /* TODO */ },
            label = { Text("Veterinarias") },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = false,
            onClick = { /* TODO */ },
            label = { Text("Productos") },
            modifier = Modifier.weight(1f)
        )
    }
}
