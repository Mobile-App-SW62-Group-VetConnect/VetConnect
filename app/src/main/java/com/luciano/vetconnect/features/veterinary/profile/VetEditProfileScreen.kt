package com.luciano.vetconnect.features.veterinary.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.shared.data.models.backendmodels.BusinessHour
import com.luciano.vetconnect.shared.data.models.backendmodels.VetCenterResponse
import com.luciano.vetconnect.shared.ui.components.veterinary.BusinessHours
import com.luciano.vetconnect.shared.ui.components.veterinary.BusinessHoursItem
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetEditProfileScreen(
    navController: NavController,
    viewModel: VetEditProfileViewModel = viewModel(
        factory = VetEditProfileViewModel.provideFactory()
    )
) {
    val profileState by viewModel.profileState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    var nombreClinica by remember { mutableStateOf("") }
    var ruc by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var businessHours by remember { mutableStateOf(listOf<BusinessHour>()) }

    // Mantenemos una referencia al perfil original
    var originalVetInfo by remember { mutableStateOf<VetCenterResponse?>(null) }

    // Cargar datos cuando llegue el perfil
    LaunchedEffect(profileState) {
        if (profileState is VetEditProfileState.Success) {
            val vetInfo = (profileState as VetEditProfileState.Success).vetInfo
            originalVetInfo = vetInfo
            nombreClinica = vetInfo.name
            email = vetInfo.contact.email
            telefono = vetInfo.contact.phone
            direccion = vetInfo.address
            descripcion = vetInfo.description ?: ""
            businessHours = vetInfo.businessHours
        }
    }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Manejar estado de actualización
    LaunchedEffect(updateState) {
        when (updateState) {
            is UpdateState.Success -> {
                showSuccessDialog = true
            }
            is UpdateState.Error -> {
                // Aquí podrías mostrar un Snackbar con el error
            }
            else -> {}
        }
    }

    var showDiscardDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf<Triple<BusinessHour, Boolean, Int>?>(null) }

    // Función para verificar cambios antes de salir
    fun checkForUnsavedChanges(onConfirmNavigate: () -> Unit) {
        if (originalVetInfo != null && hasChanges(
                originalVetInfo!!,
                nombreClinica,
                email,
                telefono,
                direccion,
                descripcion,
                businessHours
            )) {
            showDiscardDialog = true
        } else {
            onConfirmNavigate()
        }
    }

    // Manejar el botón de retroceso del sistema
    BackHandler {
        checkForUnsavedChanges { navController.popBackStack() }
    }

    // Diálogo de éxito
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

    // Diálogo para confirmar descartar cambios
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Descartar cambios", color = TextColors.Primary) },
            text = { Text("¿Estás seguro que deseas descartar los cambios?", color = TextColors.Primary) },
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SemanticColors.Error,
                        contentColor = TextColors.OnDark
                    )
                ) {
                    Text("Descartar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Cancelar", color = TextColors.Tertiary)
                }
            },
            containerColor = BackgroundColors.Surface
        )
    }

    // Diálogo para seleccionar hora
    showTimePickerDialog?.let { (hour, isStart, index) ->
        TimePickerDialog(
            onDismiss = { showTimePickerDialog = null },
            onTimeSelected = { time ->
                val updatedHours = businessHours.toMutableList()
                val updatedHour = updatedHours[index].copy(
                    open = if (isStart) time else updatedHours[index].open,
                    close = if (!isStart) time else updatedHours[index].close
                )
                updatedHours[index] = updatedHour
                businessHours = updatedHours
                showTimePickerDialog = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = TextColors.Primary) },
                navigationIcon = {
                    IconButton(
                        onClick = { checkForUnsavedChanges { navController.popBackStack() } }
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
        when (val state = profileState) {
            is VetEditProfileState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrandColors.Primary)
                }
            }
            is VetEditProfileState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message, color = SemanticColors.Error)
                }
            }
            is VetEditProfileState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = nombreClinica,
                        onValueChange = { nombreClinica = it },
                        label = { Text("Nombre de la Clínica") },
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
                        label = { Text("Correo electrónico") },
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
                        label = { Text("Teléfono") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandColors.Primary,
                            unfocusedBorderColor = NeutralColors.Gray2
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sección de horarios
                    Text(
                        "Horarios de atención",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColors.Primary,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 16.dp)
                    )

                    businessHours.forEachIndexed { index, hours ->
                        BusinessHoursItem(
                            hours = BusinessHours(
                                dayRange = hours.days,
                                startTime = hours.open,
                                endTime = hours.close
                            ),
                            isEditable = true,
                            onStartTimeClick = {
                                showTimePickerDialog = Triple(hours, true, index)
                            },
                            onEndTimeClick = {
                                showTimePickerDialog = Triple(hours, false, index)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                name = nombreClinica,
                                email = email,
                                ruc = null, // No modificamos el RUC
                                phone = telefono,
                                description = descripcion,
                                address = direccion,
                                businessHours = businessHours
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandColors.Primary,
                            contentColor = TextColors.OnDark
                        ),
                        enabled = updateState !is UpdateState.Loading
                    ) {
                        if (updateState is UpdateState.Loading) {
                            CircularProgressIndicator(
                                color = TextColors.OnDark,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Guardar cambios")
                        }
                    }
                }
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

private fun hasChanges(
    originalVetInfo: VetCenterResponse,
    currentNombreClinica: String,
    currentEmail: String,
    currentTelefono: String,
    currentDireccion: String,
    currentDescripcion: String,
    currentBusinessHours: List<BusinessHour>
): Boolean {
    // Comparar nombre de la clínica
    if (currentNombreClinica != originalVetInfo.name) {
        return true
    }

    // Comparar email
    if (currentEmail != originalVetInfo.contact.email) {
        return true
    }

    // Comparar teléfono
    if (currentTelefono != originalVetInfo.contact.phone) {
        return true
    }

    // Comparar dirección
    if (currentDireccion != originalVetInfo.address) {
        return true
    }

    // Comparar descripción
    if (currentDescripcion != (originalVetInfo.description ?: "")) {
        return true
    }

    // Comparar horarios de atención
    if (currentBusinessHours.size != originalVetInfo.businessHours.size) {
        return true
    }

    // Comparar cada horario individualmente
    currentBusinessHours.forEachIndexed { index, currentHour ->
        val originalHour = originalVetInfo.businessHours[index]
        if (currentHour.days != originalHour.days ||
            currentHour.open != originalHour.open ||
            currentHour.close != originalHour.close) {
            return true
        }
    }

    return false
}