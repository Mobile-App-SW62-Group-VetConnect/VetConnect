package com.luciano.vetconnect.features.client.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.models.Veterinary
import com.luciano.vetconnect.shared.data.models.VeterinaryService
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.ui.components.client.VetCard
import com.luciano.vetconnect.shared.ui.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.luciano.vetconnect.shared.data.api.ApiResult
import kotlinx.coroutines.launch

data class VeterinaryWithServices(
    val veterinary: Veterinary,
    val services: List<VeterinaryService>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var veterinariesData by remember { mutableStateOf<List<VeterinaryWithServices>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val repository = remember { VeterinaryRepository.getInstance() }
    val scope = rememberCoroutineScope()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    suspend fun loadVeterinariesWithServices() {
        when (val veterinariesResult = repository.getVeterinaries()) {
            is ApiResult.Success -> {
                val veterinariesWithServicesList = mutableListOf<VeterinaryWithServices>()
                veterinariesResult.data.forEach { veterinary ->
                    when (val servicesResult = repository.getServicesForVeterinary(veterinary.id)) {
                        is ApiResult.Success -> {
                            veterinariesWithServicesList.add(
                                VeterinaryWithServices(
                                    veterinary = veterinary,
                                    services = servicesResult.data
                                )
                            )
                        }
                        is ApiResult.Error -> {
                            veterinariesWithServicesList.add(
                                VeterinaryWithServices(
                                    veterinary = veterinary,
                                    services = emptyList()
                                )
                            )
                        }
                    }
                }
                veterinariesData = veterinariesWithServicesList
                error = null
            }
            is ApiResult.Error -> {
                error = veterinariesResult.message
            }
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        loadVeterinariesWithServices()
        isLoading = false
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    loadVeterinariesWithServices()
                    isRefreshing = false
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BrandColors.Primary)
                        }
                    }
                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = error!!,
                                color = SemanticColors.Error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    veterinariesData.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron veterinarias",
                                color = TextColors.Secondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = "Veterinarias cerca de ti",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextColors.Primary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            items(veterinariesData) { vetData ->
                                VetCard(
                                    veterinary = vetData.veterinary,
                                    services = vetData.services,
                                    navController = navController,
                                    onFavoriteClick = { /* Implementar lógica de favoritos */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}