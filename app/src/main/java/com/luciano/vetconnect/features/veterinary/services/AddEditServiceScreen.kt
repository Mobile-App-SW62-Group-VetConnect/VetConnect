package com.luciano.vetconnect.features.veterinary.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciano.vetconnect.shared.ui.theme.BackgroundColors
import com.luciano.vetconnect.shared.ui.theme.BrandColors
import com.luciano.vetconnect.shared.ui.theme.NeutralColors
import com.luciano.vetconnect.shared.ui.theme.TextColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditServiceScreen(
    navController: NavController,
    serviceId: String? = null
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    // Si hay serviceId, cargar los datos del servicio
    //LaunchedEffect(serviceId) {
      //  if (serviceId != null) {
       //     val service = sampleServices.find { it.id == serviceId }
       //     service?.let {
       //         name = it.name
       //         description = it.description
        //        price = it.price.toString()
        //        duration = it.duration?.toString() ?: ""
         //   }
       // }
   // }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (serviceId == null) "Nuevo servicio" else "Editar servicio",
                        color = TextColors.Primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del servicio") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio (S/)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración (minutos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandColors.Primary,
                    unfocusedBorderColor = NeutralColors.Gray2
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Aquí iría la lógica para guardar el servicio
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.Primary,
                    contentColor = TextColors.OnDark
                )
            ) {
                Text(if (serviceId == null) "Crear servicio" else "Guardar cambios")
            }
        }
    }
}