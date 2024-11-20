package com.luciano.vetconnect.features.auth.register

import RegisterViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory(
            repository = VeterinaryRepository.getInstanceReal()
        )
    )
) {val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var userType by remember { mutableStateOf("CLIENTE") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Campos específicos para Cliente
    var nombres by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    // Campos específicos para Veterinaria
    var nombreClinica by remember { mutableStateOf("") }
    var ruc by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }

    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }
    Surface(
            modifier = Modifier.fillMaxSize(),
    color = BackgroundColors.Primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.vet_connect_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 16.dp)
            )

            // Título
            Text(
                text = "Crear una cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botones de selección de tipo de usuario
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { userType = "CLIENTE" },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (userType == "CLIENTE") BrandColors.Primary else BackgroundColors.Primary,
                        contentColor = if (userType == "CLIENTE") TextColors.OnDark else TextColors.Primary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("CLIENTE")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { userType = "VETERINARIA" },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (userType == "VETERINARIA") BrandColors.Primary else BackgroundColors.Primary,
                        contentColor = if (userType == "VETERINARIA") TextColors.OnDark else TextColors.Primary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("VETERINARIA")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Campos comunes
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2,
                    focusedLabelColor = BrandColors.Primary
                ),
                enabled = registerState !is RegisterState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2,
                    focusedLabelColor = BrandColors.Primary
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        enabled = registerState !is RegisterState.Loading
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility_off
                                else R.drawable.ic_visibility
                            ),
                            contentDescription = if (passwordVisible) "Ocultar contraseña"
                            else "Mostrar contraseña",
                            tint = BrandColors.Primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2,
                    focusedLabelColor = BrandColors.Primary
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                        enabled = registerState !is RegisterState.Loading
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (confirmPasswordVisible) R.drawable.ic_visibility_off
                                else R.drawable.ic_visibility
                            ),
                            contentDescription = if (confirmPasswordVisible) "Ocultar contraseña"
                            else "Mostrar contraseña",
                            tint = BrandColors.Primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState !is RegisterState.Loading
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campos específicos según el tipo de usuario
            if (userType == "CLIENTE") {
                ClientFields(
                    nombres = nombres,
                    onNombresChange = { nombres = it },
                    dni = dni,
                    onDniChange = { dni = it },
                    telefono = telefono,
                    onTelefonoChange = { telefono = it },
                    direccion = direccion,
                    onDireccionChange = { direccion = it },
                    enabled = registerState !is RegisterState.Loading,
                    focusManager = focusManager
                )
            } else {
                VeterinaryFields(
                    nombreClinica = nombreClinica,
                    onNombreClinicaChange = { nombreClinica = it },
                    ruc = ruc,
                    onRucChange = { ruc = it },
                    licencia = licencia,
                    onLicenciaChange = { licencia = it },
                    telefono = telefono,
                    onTelefonoChange = { telefono = it },
                    direccion = direccion,
                    onDireccionChange = { direccion = it },
                    enabled = registerState !is RegisterState.Loading,
                    focusManager = focusManager
                )
            }

            if (registerState is RegisterState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (registerState as RegisterState.Error).message,
                    color = SemanticColors.Error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))// Botón de registro
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (password != confirmPassword) {
                        viewModel.setError("Las contraseñas no coinciden")
                        return@Button
                    }
                    if (userType == "CLIENTE") {
                        viewModel.registerClient(
                            email = email,
                            password = password,
                            name = nombres,
                            dni = dni,
                            phone = telefono,
                            address = direccion
                        )
                    } else {
                        viewModel.registerVeterinary(
                            email = email,
                            password = password,
                            clinicName = nombreClinica,
                            ruc = ruc,
                            license = licencia,
                            address = direccion,
                            phone = telefono
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = registerState !is RegisterState.Loading
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(
                        color = TextColors.OnDark,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("REGISTRARSE")
                }
            }

            // Link para ir al login
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = TextColors.Secondary
                )
                Text(
                    text = "Iniciar Sesión",
                    color = TextColors.Tertiary,
                    modifier = Modifier.clickable(enabled = registerState !is RegisterState.Loading) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
@Composable
private fun ClientFields(
    nombres: String,
    onNombresChange: (String) -> Unit,
    dni: String,
    onDniChange: (String) -> Unit,
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    direccion: String,
    onDireccionChange: (String) -> Unit,
    enabled: Boolean,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = nombres,
        onValueChange = onNombresChange,
        label = { Text("Nombres y Apellidos") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = dni,
        onValueChange = onDniChange,
        label = { Text("DNI") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = telefono,
        onValueChange = onTelefonoChange,
        label = { Text("Teléfono") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = direccion,
        onValueChange = onDireccionChange,
        label = { Text("Dirección (Opcional)") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )
}
@Composable
private fun VeterinaryFields(
    nombreClinica: String,
    onNombreClinicaChange: (String) -> Unit,
    ruc: String,
    onRucChange: (String) -> Unit,
    licencia: String,
    onLicenciaChange: (String) -> Unit,
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    direccion: String,
    onDireccionChange: (String) -> Unit,
    enabled: Boolean,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = nombreClinica,
        onValueChange = onNombreClinicaChange,
        label = { Text("Nombre de la Clínica") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = ruc,
        onValueChange = onRucChange,
        label = { Text("RUC") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = licencia,
        onValueChange = onLicenciaChange,
        label = { Text("Número de Licencia") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = telefono,
        onValueChange = onTelefonoChange,
        label = { Text("Teléfono") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = direccion,
        onValueChange = onDireccionChange,
        label = { Text("Dirección") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandColors.Primary,
            unfocusedBorderColor = NeutralColors.Gray2,
            focusedLabelColor = BrandColors.Primary
        ),
        enabled = enabled
    )
}