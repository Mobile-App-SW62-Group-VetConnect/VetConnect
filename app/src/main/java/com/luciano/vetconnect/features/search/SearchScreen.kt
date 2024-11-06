package com.luciano.vetconnect.features.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luciano.vetconnect.navigation.Screen
import com.luciano.vetconnect.shared.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun performSearch() {
        if (searchText.isNotBlank()) {
            keyboardController?.hide()
            viewModel.search(searchText)
        }
    }

    LaunchedEffect(searchState) {
        if (searchState is SearchState.Success) {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                "searchResults",
                (searchState as SearchState.Success).results
            )
            navController.navigate(Screen.SearchResults.route)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = BrandColors.Secondary
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = BackgroundColors.Primary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = TextColors.Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                BasicTextField(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    modifier = Modifier.weight(1f),
                                    decorationBox = { innerTextField ->
                                        Box {
                                            if (searchText.isEmpty()) {
                                                Text(
                                                    "¿Qué estás buscando?",
                                                    color = TextColors.Secondary
                                                )
                                            }
                                            innerTextField()
                                        }
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = { performSearch() })
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundColors.Secondary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (searchState) {
                is SearchState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BrandColors.Primary
                    )
                }
                is SearchState.Error -> {
                    Text(
                        text = (searchState as SearchState.Error).message,
                        color = SemanticColors.Error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    if (searchText.isBlank()) {
                        Text(
                            text = "¿Qué veterinaria estás buscando?",
                            color = TextColors.Secondary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}