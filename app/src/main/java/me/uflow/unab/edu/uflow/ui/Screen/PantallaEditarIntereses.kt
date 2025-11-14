package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel

// Lista de todos los intereses disponibles en la app
private val todosLosIntereses = listOf(
    "Kotlin", "Java", "Python", "Desarrollo Web",
    "Ciberseguridad", "Inteligencia Artificial", "Machine Learning",
    "Desarrollo de Videojuegos", "Música", "Películas", "Deportes",
    "Historia", "Arte", "Ciencia", "Viajar"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PantallaEditarIntereses(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    val userData = uiState.userData

    val selectedInterests = remember(userData) {
        mutableStateOf(userData?.intereses?.toSet() ?: emptySet())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Intereses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                item {
                    Text(
                        "Selecciona los temas que te gustan",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                item {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        todosLosIntereses.forEach { interes ->
                            val isSelected = selectedInterests.value.contains(interes)

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    val currentSet = selectedInterests.value.toMutableSet()
                                    if (isSelected) {
                                        currentSet.remove(interes)
                                    } else {
                                        currentSet.add(interes)
                                    }
                                    selectedInterests.value = currentSet
                                },
                                label = { Text(interes) },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Seleccionado",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }
            }

            // Botón de Guardar
            Button(
                onClick = {
                    authViewModel.updateUserInterests(selectedInterests.value.toList())
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}