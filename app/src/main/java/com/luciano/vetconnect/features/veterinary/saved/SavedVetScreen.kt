package com.luciano.vetconnect.features.veterinary.saved

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
import com.luciano.vetconnect.shared.ui.theme.*
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.ui.components.VetCard
import com.luciano.vetconnect.shared.ui.components.FilterButtons


@Composable
fun SavedVetScreen(navController: NavController, onMenuClick: () -> Unit) {
    val context = LocalContext.current
    var veterinaries by remember { mutableStateOf<List<Veterinary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }



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
                            //items(veterinaries) { veterinary ->
                               // VetCard(
                                //    veterinary = veterinary,
                                //    navController = navController,
                                //    onFavoriteClick = { /* Implementar lógica de favoritos */ }
                               // )
                            }
                        }
                    }
                }
            }
        }
    }
//}
