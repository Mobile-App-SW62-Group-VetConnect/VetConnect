package com.luciano.vetconnect.features.veterinary.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.ui.theme.*
import com.luciano.vetconnect.shared.utils.UserManager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeVetScreen(
    navController: NavController,
    onMenuClick: () -> Unit,
    repository: VeterinaryRepository
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(repository)
    )
    val coroutineScope = rememberCoroutineScope()
    val veterinaryInfo by viewModel.veterinaryInfo.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        val token = UserManager.getToken()
        if (token != null) {
            viewModel.fetchVeterinaryInfo(vetId = 2, token = token)
        } else {
            println("Error: El token es nulo")
        }
    }

    Scaffold(
        topBar = { TopAppBar(onMenuClick = onMenuClick) },
        containerColor = BackgroundColors.Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            veterinaryInfo?.let { vetInfo ->
                // Card de la Veterinaria
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = BackgroundColors.Surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Carrusel de fotos
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            val pagerState = rememberPagerState(pageCount = { 1 })

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = rememberImagePainter(vetInfo.imageProfile),
                                    contentDescription = "Foto del local",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        // Rating y reviews
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = if (index < 4) Icons.Default.Star
                                    else Icons.Default.StarHalf,
                                    contentDescription = null,
                                    tint = BrandColors.Secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "233 Reviews",
                                color = TextColors.Secondary,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = vetInfo.name?: "Nombre no disponible",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = BrandColors.Primary
                        )

                        Text(
                            text = vetInfo.description?: "Descripción no disponible",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextColors.Primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Sección de Últimas Reseñas
            Text(
                text = "Últimas Reseñas",
                style = MaterialTheme.typography.titleMedium,
                color = TextColors.Primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            // Card de Reseñas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    repeat(2) {
                        ReviewItem()
                        if (it < 1) Divider(
                            color = NeutralColors.Gray1,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewItem() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_avatar),
                contentDescription = "Avatar del usuario",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = "Nombre de usuario",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColors.Primary
                )
                Text(
                    text = "Hace x días",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextColors.Secondary
                )
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            repeat(4) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = BrandColors.Secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = NeutralColors.Gray2,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = "Texto",
            style = MaterialTheme.typography.bodyMedium,
            color = TextColors.Primary
        )
    }
}