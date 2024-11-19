package com.luciano.vetconnect.features.client.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.ui.components.client.FilterButtons
import com.luciano.vetconnect.shared.ui.components.client.VetCard
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(modelClass = SearchViewModel::class.java)
) {
    val searchState by viewModel.searchState.collectAsState()
    val results = navController.previousBackStackEntry?.savedStateHandle?.get<List<VeterinaryWithServices>>("searchResults")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados de búsqueda", color = TextColors.Primary) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // volver a la pantalla de búsqueda
                            navController.navigate(Screen.Search.route) {
                                popUpTo(Screen.Search.route) { inclusive = true }
                            }
                        }
                    ) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterButtons()

            when {
                results == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay resultados disponibles",
                            color = TextColors.Secondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                results.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron resultados",
                            color = TextColors.Secondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Encontramos ${results.size} resultados",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = TextColors.Primary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(results) { result ->
                                VetCard(
                                    veterinary = result.veterinary,
                                    services = result.services,
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