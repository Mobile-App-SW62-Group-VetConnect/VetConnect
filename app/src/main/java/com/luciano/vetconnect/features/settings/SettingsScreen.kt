package com.luciano.vetconnect.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.navigation.TopAppBar
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun SettingsScreen(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión", color = TextColors.Primary) },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?", color = TextColors.Primary) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Sí, cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = TextColors.Tertiary)
                }
            },
            containerColor = BackgroundColors.Surface
        )
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
            Text(
                text = "Ajustes",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary,
                modifier = Modifier.padding(16.dp)
            )

            SettingsSection("Cuenta") {
                SettingsItem(
                    icon = Icons.Outlined.Person,
                    title = "Editar perfil",
                    onClick = { navController.navigate(Screen.EditProfile.route) }
                )
                SettingsItem(
                    icon = Icons.Outlined.Lock,
                    title = "Cambiar contraseña",
                    onClick = { navController.navigate(Screen.ChangePassword.route) }
                )
                SettingsItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notificaciones",
                    onClick = { navController.navigate(Screen.NotificationSettings.route) }
                )
            }

            SettingsSection("Preferencias") {
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    title = "Idioma",
                    subtitle = "Español",
                    onClick = { /* TODO: Show language picker */ }
                )
                SettingsItem(
                    icon = Icons.Outlined.DarkMode,
                    title = "Tema",
                    subtitle = "Claro",
                    onClick = { /* TODO: Show theme picker */ }
                )
            }

            SettingsSection("Información") {
                SettingsItem(
                    icon = Icons.Outlined.Info,
                    title = "Acerca de",
                    onClick = { /* TODO: Show about dialog */ }
                )
                SettingsItem(
                    icon = Icons.Outlined.Policy,
                    title = "Política de privacidad",
                    onClick = { /* TODO: Navigate to privacy policy */ }
                )
                SettingsItem(
                    icon = Icons.Outlined.Security,
                    title = "Términos de servicio",
                    onClick = { /* TODO: Navigate to terms */ }
                )
            }

            SettingsSection {
                SettingsItem(
                    icon = Icons.Outlined.Logout,
                    title = "Cerrar sesión",
                    titleColor = SemanticColors.Error,
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
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
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = TextColors.Primary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandColors.Primary,
            modifier = Modifier.size(24.dp)
        )
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
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = NeutralColors.Gray2
        )
    }
}