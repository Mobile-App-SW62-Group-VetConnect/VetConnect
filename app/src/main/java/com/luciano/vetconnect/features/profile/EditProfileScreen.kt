package com.luciano.vetconnect.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.theme.*
import com.luciano.vetconnect.shared.utils.UserManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController
) {
    val currentUser by UserManager.currentUser.collectAsState()

    var nombre by remember { mutableStateOf(currentUser?.name ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var telefono by remember { mutableStateOf(currentUser?.phone ?: "") }
    var direccion by remember { mutableStateOf(currentUser?.address ?: "") }

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
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                IconButton(
                    onClick = { /* TODO: Implementar cambio de foto */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandColors.Primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Cambiar foto",
                        tint = TextColors.OnDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = TextColors.Primary) },
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

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
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