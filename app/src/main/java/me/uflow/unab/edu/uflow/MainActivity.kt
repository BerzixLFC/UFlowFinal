package me.uflow.unab.edu.uflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column 
import androidx.compose.foundation.layout.Spacer 
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height 
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button 
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight 
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.viewmodel.ChatViewModel
import me.uflow.unab.edu.uflow.viewmodel.QuizViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import me.uflow.unab.edu.uflow.ui.Screen.AmigosScreen
import me.uflow.unab.edu.uflow.ui.Screen.Calendario
import me.uflow.unab.edu.uflow.ui.Screen.ChallengeApp
import me.uflow.unab.edu.uflow.ui.Screen.PantallaAsistencia
import me.uflow.unab.edu.uflow.ui.Screen.PantallaDetalleCurso
import me.uflow.unab.edu.uflow.ui.Screen.PantallaLoginCompleta
import me.uflow.unab.edu.uflow.ui.Screen.PantallaMaquetacionInicioInlineTotal
import me.uflow.unab.edu.uflow.ui.Screen.PantallaMenuPrincipal
import me.uflow.unab.edu.uflow.ui.Screen.PantallaPerfil
import me.uflow.unab.edu.uflow.ui.Screen.PantallaPerfilAmigo
import me.uflow.unab.edu.uflow.ui.Screen.PantallaRegistroUflowHardcode
import me.uflow.unab.edu.uflow.ui.Screen.PomodoroScreen
import me.uflow.unab.edu.uflow.ui.Screen.TaskDetailScreen
import me.uflow.unab.edu.uflow.ui.screens.PantallaChatDirecto
import me.uflow.unab.edu.uflow.ui.screens.PantallaCursosCompletados
import me.uflow.unab.edu.uflow.ui.screens.PantallaEditarConexiones
import me.uflow.unab.edu.uflow.ui.screens.PantallaEditarInformacion
import me.uflow.unab.edu.uflow.ui.screens.PantallaEditarIntereses
import me.uflow.unab.edu.uflow.ui.screens.PantallaLeccionActiva
import me.uflow.unab.edu.uflow.ui.screens.PantallaSeleccionDificultad
import me.uflow.unab.edu.uflow.ui.screens.PantallaSeleccionLenguaje
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.viewmodel.EventoViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    val authState by authViewModel.uiState.collectAsState()

    val navController = rememberNavController()

    if (authState.isInitializing) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val localUserData = authState.userData
        val startDestination = if (localUserData != null) {
            if (localUserData.selectedLanguage != null) "menu" else "seleccion_lenguaje"
        } else {
            "inicio"
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable("inicio") {
                PantallaMaquetacionInicioInlineTotal(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToRegister = { navController.navigate("registro") }
                )
            }
            composable("login") {
                PantallaLoginCompleta(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate("registro") },
                    onNavigateToForgotPassword = { navController.navigate("contacto") },
                    onNavigateToContact = { navController.navigate("contacto") }
                )
                LaunchedEffect(authState.userData) {
                    val localUserDataInEffect = authState.userData
                    if (localUserDataInEffect != null && localUserDataInEffect.selectedLanguage == null) {
                        navController.navigate("seleccion_lenguaje") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }
                }
            }
            composable("registro") {
                PantallaRegistroUflowHardcode(
                    viewModel = authViewModel,
                    onNavigateToLogin = {
                        navController.navigate("login") { popUpTo("inicio") }
                    }
                )
                LaunchedEffect(authState.userData) {
                    val localUserDataInEffect = authState.userData
                    if (localUserDataInEffect != null && localUserDataInEffect.selectedLanguage == null) {
                        navController.navigate("seleccion_lenguaje") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }
                }
            }
            composable("seleccion_lenguaje") {
                PantallaSeleccionLenguaje(navController = navController)
            }
            composable(
                route = "seleccion_dificultad/{lenguajeName}",
                arguments = listOf(navArgument("lenguajeName") { type = NavType.StringType })
            ) { backStackEntry ->
                val lenguajeName = backStackEntry.arguments?.getString("lenguajeName")
                val lenguaje = Lenguaje.entries.find { it.name == lenguajeName } ?: Lenguaje.PYTHON
                PantallaSeleccionDificultad(
                    lenguaje = lenguaje,
                    navController = navController
                )
            }

            composable("menu") {
                PantallaMenuPrincipal(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
            composable("asistencia") {
                PantallaAsistencia(
                    navController = navController
                )
            }
            composable("perfil") {
                PantallaPerfil(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
            composable("calendario") {
                Calendario(
                    navController= navController,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("amigos") {
                AmigosScreen(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
            composable(
                route = "chat/{friendUid}/{friendName}",
                arguments = listOf(
                    navArgument("friendUid") { type = NavType.StringType },
                    navArgument("friendName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val friendUid = backStackEntry.arguments?.getString("friendUid")
                val friendName = backStackEntry.arguments?.getString("friendName")
                val currentUserUid = authState.userData?.uid

                if (currentUserUid != null && friendUid != null && friendName != null) {
                    PantallaChatDirecto(
                        navController = navController,
                        currentUserUid = currentUserUid,
                        friendUid = friendUid,
                        friendName = friendName
                    )
                } else {
                    navController.popBackStack()
                }
            }

            composable(
                route = "amigo_perfil/{friendUid}",
                arguments = listOf(
                    navArgument("friendUid") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val friendUid = backStackEntry.arguments?.getString("friendUid")
                if (friendUid != null) {
                    PantallaPerfilAmigo(
                        navController = navController,
                        authViewModel = authViewModel,
                        friendUid = friendUid
                    )
                } else {
                    navController.popBackStack()
                }
            }

            composable(
                route = "curso_detalle/{lenguajeName}/{courseId}",
                arguments = listOf(
                    navArgument("lenguajeName") { type = NavType.StringType },
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val lenguajeName = backStackEntry.arguments?.getString("lenguajeName") ?: Lenguaje.KOTLIN.name
                val courseId = backStackEntry.arguments?.getString("courseId") ?: "kotlin_cero"

                PantallaDetalleCurso(
                    navController = navController,
                    courseId = courseId,
                    lenguajeName = lenguajeName,
                    authViewModel = authViewModel
                )
            }

            composable(
                route = "leccion_activa/{courseId}/{sessionId}/{lenguajeName}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("sessionId") { type = NavType.StringType },
                    navArgument("lenguajeName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: "kotlin_cero"
                val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
                val lenguajeName = backStackEntry.arguments?.getString("lenguajeName") ?: Lenguaje.KOTLIN.name

                PantallaLeccionActiva(
                    navController = navController,
                    authViewModel = authViewModel,
                    courseId = courseId,
                    sessionId = sessionId,
                    lenguajeName = lenguajeName
                )
            }
            composable("reto") {
                ChallengeApp(
                    navController = navController,
                    viewModel = quizViewModel,
                    authViewModel = authViewModel 
                )
            }
            
            composable("editar_conexiones") {
                PantallaEditarConexiones(navController = navController, authViewModel = authViewModel)
            }

            composable("cursos_completados") {
                PantallaCursosCompletados(navController = navController, authViewModel = authViewModel)
            }

            composable("editar_intereses") {
                PantallaEditarIntereses(navController = navController)
            }

            composable("editar_informacion") {
                PantallaEditarInformacion(navController = navController)
            }
            
            composable("contacto") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Pantalla de Contacto/Ayuda",
                        modifier = Modifier.padding(50.dp),
                        fontSize = 20.sp
                    )
                }
            }
            
            composable("pomodoro") {
                PomodoroScreen(navController = navController)
            }
            
            composable("habitos") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Pantalla de Hábitos",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("¡Próximamente!")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Regresar")
                        }
                    }
                }
            }
            // En tu NavHost
            composable("taskDetail/{date}") { backStackEntry ->
                val dateStr = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                val date = LocalDate.parse(dateStr)
                val taskViewModel: EventoViewModel = viewModel()
                TaskDetailScreen(
                    onBack = { navController.popBackStack() },
                    initialDate = date,
                    onSaveTask = { title, details, subject ->
                        taskViewModel.addTask(title, details, subject, date)
                        navController.popBackStack()
                    }
                )
            }

        }
    }
}