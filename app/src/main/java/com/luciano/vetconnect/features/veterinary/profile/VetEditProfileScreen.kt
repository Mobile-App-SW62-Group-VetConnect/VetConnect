package com.luciano.vetconnect.features.veterinary.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.components.BusinessHours
import com.luciano.vetconnect.shared.ui.components.BusinessHoursItem
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetEditProfileScreen(
    navController: NavController
) {
    var nombreClinica by remember { mutableStateOf("Clínica Veterinaria - El Roble") }
    var ruc by remember { mutableStateOf("20123456789") }
    var email by remember { mutableStateOf("contacto@elroble.com") }
    var telefono by remember { mutableStateOf("01 234 5678") }
    var direccion by remember { mutableStateOf("Jr. Las Palmeras 123, Lima") }

    var businessHours by remember { mutableStateOf(listOf(
        BusinessHours("Lunes a Viernes", "09:00", "19:00"),
        BusinessHours("Sábados", "09:00", "14:00"),
        BusinessHours("Domingos y Feriados", "Cerrado", "Cerrado")
    )) }

    var photos by remember { mutableStateOf(listOf(
        R.drawable.vet_clinic,
        R.drawable.vet_clinic,
        R.drawable.vet_clinic
    )) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf<Triple<BusinessHours, Boolean, Int>?>(null) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.popBackStack()
            },
            title = { Text("Perfil actualizado", color = TextColors.Primary) },
            text = { Text("Los cambios han sido guardados exitosamente", color = TextColors.Primary) },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Aceptar")
                }
            },
            containerColor = BackgroundColors.Surface
        )
    }

    showTimePickerDialog?.let { (hours, isStart, index) ->
        TimePickerDialog(
            onDismiss = { showTimePickerDialog = null },
            onTimeSelected = { time ->
                businessHours = businessHours.toMutableList().apply {
                    this[index] = if (isStart) {
                        hours.copy(startTime = time)
                    } else {
                        hours.copy(endTime = time)
                    }
                }
                showTimePickerDialog = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = TextColors.Primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Galería de fotos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Fotos del local",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(photos) { photo ->
                            Box(
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Image(
                                    painter = painterResource(id = photo),
                                    contentDescription = "Foto del local",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = {
                                        photos = photos.toMutableList().apply {
                                            remove(photo)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(SemanticColors.Error)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Eliminar foto",
                                        tint = TextColors.OnDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeutralColors.Gray1)
                                    .clickable { /* TODO: Implement photo upload */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar foto",
                                    tint = TextColors.Secondary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información básica
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información básica",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = nombreClinica,
                        onValueChange = { nombreClinica = it },
                        label = { Text("Nombre de la Clínica", color = TextColors.Primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = ruc,
                        onValueChange = { ruc = it },
                        label = { Text("RUC", color = TextColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = TextColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono", color = TextColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección", color = TextColors.Primary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horarios de atención
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BackgroundColors.Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Horarios de atención",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    businessHours.forEachIndexed { index, hours ->
                        BusinessHoursItem(
                            hours = hours,
                            isEditable = true,
                            onStartTimeClick = {
                                if (hours.startTime != "Cerrado") {
                                    showTimePickerDialog = Triple(hours, true, index)
                                }
                            },
                            onEndTimeClick = {
                                if (hours.endTime != "Cerrado") {
                                    showTimePickerDialog = Triple(hours, false, index)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Guardar cambios")
            }
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar hora", color = TextColors.Primary) },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedHour.toString(),
                        onValueChange = {
                            val newValue = it.toIntOrNull()
                            if (newValue != null && newValue in 0..23) {
                                selectedHour = newValue
                            }
                        },
                        label = { Text("Hora", color = TextColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    OutlinedTextField(
                        value = selectedMinute.toString(),
                        onValueChange = {
                            val newValue = it.toIntOrNull()
                            if (newValue != null && newValue in 0..59) {
                                selectedMinute = newValue
                            }
                        },
                        label = { Text("Minutos", color = TextColors.Primary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
                    onTimeSelected(timeString)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                )
            ) {
                Text("Aceptar")
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