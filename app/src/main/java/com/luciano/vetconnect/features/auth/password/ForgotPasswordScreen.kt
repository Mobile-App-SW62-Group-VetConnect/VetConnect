// ForgotPasswordScreen.kt
package com.luciano.vetconnect.features.auth.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColors.Primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = TextColors.Primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (!isEmailSent) {
                Text(
                    text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para recuperar tu contraseña",
                    color = TextColors.Primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandColors.Primary,
                        unfocusedBorderColor = NeutralColors.Gray2
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { isEmailSent = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Enviar instrucciones")
                }
            } else {
                Text(
                    text = "¡Correo enviado!",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandColors.Primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Hemos enviado las instrucciones de recuperación de contraseña a tu correo electrónico. Por favor, revisa tu bandeja de entrada.",
                    color = TextColors.Primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandColors.Primary,
                        contentColor = TextColors.OnDark
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Volver al inicio de sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text(
                    text = "Volver",
                    color = TextColors.Tertiary
                )
            }
        }
    }
}