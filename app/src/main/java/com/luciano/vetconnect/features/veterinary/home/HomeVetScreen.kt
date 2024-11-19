package com.luciano.vetconnect.features.veterinary.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeVetScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val photos = listOf(
        R.drawable.vet_clinic,
        R.drawable.vet_clinic,
        R.drawable.vet_clinic
    )

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
            // Saludo
            Text(
                text = "¡Bienvenido! [username]",
                style = MaterialTheme.typography.titleLarge,
                color = TextColors.Primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

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
                        val pagerState = rememberPagerState(pageCount = { photos.size })

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            Image(
                                painter = painterResource(id = photos[page]),
                                contentDescription = "Foto del local ${page + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Botones de navegación
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (pagerState.currentPage > 0) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(BrandColors.Primary.copy(alpha = 0.7f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronLeft,
                                        contentDescription = "Anterior",
                                        tint = TextColors.OnDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }

                            if (pagerState.currentPage < photos.size - 1) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(BrandColors.Primary.copy(alpha = 0.7f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Siguiente",
                                        tint = TextColors.OnDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }
                        }

                        // Indicadores de página
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(photos.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) {
                                    BrandColors.Primary
                                } else {
                                    TextColors.OnDark.copy(alpha = 0.5f)
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(6.dp)
                                )
                            }
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
                        text = "Clinica Veterinaria - El Roble",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = BrandColors.Primary
                    )
                }
            }

            // Card de Visitas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "128",
                        style = MaterialTheme.typography.headlineLarge,
                        color = BrandColors.Primary,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Personas han visitado",
                            color = TextColors.Primary
                        )
                        Text(
                            text = "tu perfil en las últimas 24 horas",
                            color = TextColors.Primary
                        )
                    }
                }
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