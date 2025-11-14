package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.data.model.Session
import me.uflow.unab.edu.uflow.data.model.SessionStatus
import me.uflow.unab.edu.uflow.data.repository.MasterCourseRepository 
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje

class SesionViewModel(courseId: String, sessionId: String) : ViewModel() {
    val session: Session?

    init {
        session = MasterCourseRepository.getSessionDetails(courseId, sessionId)
    }
}
class SesionViewModelFactory(
    private val courseId: String,
    private val sessionId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SesionViewModel(courseId, sessionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleSesion(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    courseId: String,
    sessionId: String,
    lenguajeName: String
) {
    val factory = SesionViewModelFactory(courseId, sessionId)
    val viewModel: SesionViewModel = viewModel(factory = factory)

    val session = viewModel.session
    val language = Lenguaje.entries.find { it.name == lenguajeName } ?: Lenguaje.KOTLIN

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            language.color.copy(alpha = 0.8f),
            language.color.copy(alpha = 0.6f)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .verticalScroll(rememberScrollState())
        ) {
            // --- Encabezado ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Text(
                    text = session?.title ?: "Cargando...",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            //Cuerpo con la descripción
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = session?.description ?: "No hay descripción.", 
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Botón de Empezar
                Button(
                    onClick = {
                        if (session != null) {
                            authViewModel.updateSessionStatus(session.id, SessionStatus.IN_PROGRESS.name) // <-- Corregido
                            
                            navController.navigate("leccion_activa/$courseId/${session.id}/$lenguajeName") // <-- Corregido
                        } else {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = language.color),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Empezar Lección", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}