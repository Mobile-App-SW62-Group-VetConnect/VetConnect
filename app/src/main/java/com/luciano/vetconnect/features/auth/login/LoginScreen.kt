package com.luciano.vetconnect.features.auth.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.data.api.RetrofitInstance
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.ui.theme.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory(
            veterinaryRepository = VeterinaryRepository.getInstance()
        )
    )
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isVetUser by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                val isVetUserLogin = (loginState as LoginState.Success).isVetUser
                if (isVetUserLogin) {
                    navController.navigate(Screen.HomeVet.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.vet_connect_logo),
                contentDescription = "Vet Connect Logo",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Switch para alternar entre cliente y veterinaria
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingresar como veterinaria",
                    color = TextColors.Primary
                )
                Switch(
                    checked = isVetUser,
                    onCheckedChange = { isVetUser = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextColors.OnDark,
                        checkedTrackColor = BrandColors.Primary,
                        uncheckedThumbColor = NeutralColors.Gray2,
                        uncheckedTrackColor = NeutralColors.Gray1
                    )
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = TextColors.Primary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = loginState !is LoginState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = TextColors.Primary) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        enabled = loginState !is LoginState.Loading
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
                enabled = loginState !is LoginState.Loading
            )

            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = SemanticColors.Error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        color = TextColors.OnDark,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("INICIAR SESIÓN")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.ForgotPassword.route) }) {
                Text("¿Olvidaste tu contraseña?", color = TextColors.Tertiary)
            }

            TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                Text("Crear una cuenta", color = TextColors.Tertiary)
            }
        }
    }
}