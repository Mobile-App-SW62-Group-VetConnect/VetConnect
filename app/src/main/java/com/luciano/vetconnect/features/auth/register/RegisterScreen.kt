package com.luciano.vetconnect.features.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun RegisterScreen(navController: NavController) {
    var userType by remember { mutableStateOf("CLIENTE") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Campos específicos para Cliente
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }

    // Campos específicos para Veterinaria
    var nombreClinica by remember { mutableStateOf("") }
    var ruc by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var licencia by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColors.Primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Añadido scroll vertical
                .padding(40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary
            )

            Spacer(modifier = Modifier.height(40.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            // Campos dinámicos según el tipo de usuario
            if (userType == "CLIENTE") {
                // Campos para Cliente
                OutlinedTextField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = { Text("Nombres", color = TextColors.Primary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos", color = TextColors.Primary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI", color = TextColors.Primary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )
            } else {
                // Campos para Veterinaria
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

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = licencia,
                    onValueChange = { licencia = it },
                    label = { Text("Número de Licencia", color = TextColors.Primary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )
            }

            // Campos comunes para ambos tipos de usuario
            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = TextColors.Primary) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = BrandColors.Primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña", color = TextColors.Primary) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = if (confirmPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                            contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = BrandColors.Primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: Implement registration logic */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REGISTRARSE")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya eres miembro?", color = TextColors.Secondary)
                Text(" Iniciar Sesión", color = TextColors.Tertiary)
            }
        }
    }
}