package com.luciano.vetconnect.shared.ui.components.privacy

import androidx.compose.runtime.Composable


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.ui.theme.*


@Composable
fun PrivacyPoliciesScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {

    var showMore by remember { mutableStateOf(false) }


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
            Text(
                text = "Politicas de Privacidad",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            SettingsSection {
                Text(
                    text = "En Vet Connect, estamos comprometidos a proteger la privacidad de nuestros usuarios. Este documento describe cómo recopilamos, utilizamos, almacenamos y compartimos tu información personal al interactuar con nuestra aplicación móvil y servicios asociados. Nuestro objetivo es garantizar que tus datos estén seguros y que tengas pleno control sobre ellos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColors.Primary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
            SettingsSection() {
                SettingsItem(
                    title = "1. Alcance de la Política de Privacidad",
                    showMore = showMore,
                    onClick = { showMore = !showMore }
                )
                if (showMore){
                    Text(
                        text = "Esta Política de Privacidad se aplica a la información personal que recopilamos a través de nuestra aplicación móvil y servicios asociados. No se aplica a la información recopilada por terceros, incluidos los anunciantes, ni a la información que recopilamos fuera de nuestra aplicación móvil y servicios asociados.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColors.Primary,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 1.dp)) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextColors.Secondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        content()
        Divider(color = NeutralColors.Gray1, thickness = 1.dp)
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String? = null,
    titleColor: Color = TextColors.Primary,
    showMore: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = TextColors.Secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Icon(
            imageVector = if(showMore) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = NeutralColors.Gray2
        )
    }
}

@Preview
@Composable
fun prevScreen(){
    val navController = rememberNavController()
    PrivacyPoliciesScreen(navController) {
    }
}