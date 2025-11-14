package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarConexiones(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    val userData = uiState.userData

    var instagram by remember { mutableStateOf(userData?.instagramHandle ?: "") }
    var twitter by remember { mutableStateOf(userData?.twitterHandle ?: "") }
    var github by remember { mutableStateOf(userData?.githubHandle ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Conexiones") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Añade tus nombres de usuario (sin el '@')",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            // Campo de Instagram
            OutlinedTextField(
                value = instagram,
                onValueChange = { instagram = it },
                label = { Text("Instagram") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo de Twitter
            OutlinedTextField(
                value = twitter,
                onValueChange = { twitter = it },
                label = { Text("Twitter (X)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo de GitHub
            OutlinedTextField(
                value = github,
                onValueChange = { github = it },
                label = { Text("GitHub") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Guardar
            Button(
                onClick = {
                    authViewModel.updateUserConnections(
                        instagram.trim(),
                        twitter.trim(),
                        github.trim()
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}