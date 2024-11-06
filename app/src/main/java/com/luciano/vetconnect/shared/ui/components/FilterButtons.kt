package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun FilterButtons(
    modifier: Modifier = Modifier
) {
    var selectedButton by remember { mutableStateOf<String?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            onClick = {
                selectedButton = if (selectedButton == "FILTER") null else "FILTER"
                showFilterDialog = true
            },
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = BrandColors.Primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            color = BackgroundColors.Primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = null,
                    tint = BrandColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Filtros",
                    fontSize = 16.sp,
                    color = BrandColors.Primary
                )
            }
        }

        Surface(
            onClick = {
                selectedButton = if (selectedButton == "SORT") null else "SORT"
                showSortDialog = true
            },
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = NeutralColors.Gray2,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            color = BackgroundColors.Primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ordenar por",
                    fontSize = 16.sp,
                    color = NeutralColors.Gray2
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = NeutralColors.Gray2,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Los diálogos se mantienen igual
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = {
                showFilterDialog = false
                selectedButton = null
            }
        )
    }

    if (showSortDialog) {
        SortDialog(
            onDismiss = {
                showSortDialog = false
                selectedButton = null
            }
        )
    }
}

@Composable
private fun FilterDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtros", color = TextColors.Primary) },
        text = {
            Column {
                Text("Calificación mínima", color = TextColors.Primary)
                Slider(
                    value = 0f,
                    onValueChange = { },
                    valueRange = 0f..5f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandColors.Primary,
                        activeTrackColor = BrandColors.Primary
                    )
                )
                Text("Precio máximo", color = TextColors.Primary)
                Slider(
                    value = 0f,
                    onValueChange = { },
                    valueRange = 0f..1000f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandColors.Primary,
                        activeTrackColor = BrandColors.Primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                )
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextColors.Tertiary)
            }
        },
        containerColor = BackgroundColors.Surface
    )
}

@Composable
private fun SortDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ordenar por", color = TextColors.Primary) },
        text = {
            Column {
                val options = listOf(
                    "Nombre (A-Z)",
                    "Nombre (Z-A)",
                    "Mayor calificación",
                    "Menor calificación",
                    "Mayor precio",
                    "Menor precio"
                )
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDismiss() }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option, color = TextColors.Primary)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextColors.Tertiary)
            }
        },
        containerColor = BackgroundColors.Surface
    )
}
