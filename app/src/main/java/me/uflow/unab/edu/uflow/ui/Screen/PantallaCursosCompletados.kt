package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCursosCompletados(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    val userData = uiState.userData

    val cursosCompletados = userData?.completedCourses ?: emptyList()
    val cursoNombres = mapOf(
        "kotlin_intro" to "Bienvenido a Kotlin",
        "kotlin_cero" to "Kotlin desde Cero"
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cursos Completados") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cursosCompletados.isEmpty()) {
                item {
                    Text("No hay cursos completados.", color = Color.Gray)
                }
            } else {
                items(cursosCompletados) { cursoId ->
                    val nombre = cursoNombres[cursoId] ?: cursoId
                    CompletedCourseGridItem(
                        title = nombre,
                        language = Lenguaje.KOTLIN // TODO: Debe ser dinámico
                    )
                }
            }
        }
    }
}
@Composable
private fun CompletedCourseGridItem(
    title: String,
    language: Lenguaje
) {
    Card(
        modifier = Modifier
            .height(120.dp), 
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF111827),
                maxLines = 3,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Text(
                text = "1 Sesiones", // TODO: Esto debe ser dinámico
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}