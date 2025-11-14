package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable 
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip 
import androidx.compose.ui.draw.shadow 
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthUiState
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.data.repository.MasterCourseRepository
import me.uflow.unab.edu.uflow.util.Nivel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PantallaMenuPrincipal(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val authState by viewModel.uiState.collectAsState()

    PantallaMenuPrincipalUI(
        navController = navController,
        authState = authState,
        onLogout = { viewModel.logout() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PantallaMenuPrincipalUI(
    navController: NavController,
    authState: AuthUiState,
    onLogout: () -> Unit
) {
    val userData = authState.userData

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(Lenguaje.PYTHON, Lenguaje.JAVA, Lenguaje.KOTLIN, Lenguaje.WEB)
    var isChatCardVisible by remember { mutableStateOf(false) }

    val selectedLanguageColor = tabs[selectedTabIndex].color
    val blendedBackgroundColor by animateColorAsState(
        targetValue = selectedLanguageColor.copy(alpha = 0.25f),
        label = "blendedBackgroundColor"
    )

    LaunchedEffect(userData?.selectedLanguage) {
        val selectedLanguage = userData?.selectedLanguage
        if (selectedLanguage != null) {
            val index = tabs.indexOf(selectedLanguage)
            if (index != -1) {
                selectedTabIndex = index
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .height(90.dp)
                    .padding(start = 16.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bienvenido,",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF5660A8)
                            )) {
                                append(userData?.usuario?.takeIf { it.isNotBlank() } ?: "Usuario")
                            }
                        },
                        fontSize = 28.sp,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .clickable { navController.navigate("perfil") },
                    contentAlignment = Alignment.Center
                ) {
                    // Aquí iría la Image(painter = ...)
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estás aquí */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {navController.navigate("asistencia") },
                    icon = { Icon(Icons.Default.Code, contentDescription = "Chat") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("amigos") },
                    icon = { Icon(Icons.Default.Group, contentDescription = "Amigos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                )
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    AnimatedVisibility(
                        visible = isChatCardVisible,
                        enter = fadeIn(animationSpec = tween(150)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 },
                        exit = fadeOut(animationSpec = tween(150)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 }
                    ) {
                        ChatBotCard()
                    }

                    FloatingActionButton(
                        onClick = { isChatCardVisible = !isChatCardVisible },
                        containerColor = Color(0xFF00C896),
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Abrir Chat",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 1. Botón RETO (VERDE)
                GradientButton(
                    text = "RETO",
                    gradientColors = listOf(Color(0xFF00C896), Color(0xFF3DDCB1)), // Verde a verde más claro
                    contentColor = Color.White,
                    onClick = { navController.navigate("reto") }
                )

                // 2. Botón CALENDARIO (Morado)
                GradientButton(
                    text = "CALENDARIO",
                    gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF7A72D3)), // Morado a morado más claro
                    contentColor = Color.White,
                    onClick = { navController.navigate("calendario") }
                )

                // 3. Botón POMODORO (Morado claro)
                GradientButton(
                    text = "POMODORO",
                    gradientColors = listOf(Color(0xFFE0E7FF), Color(0xFFFFFFFF)), // Morado claro a blanco
                    contentColor = Color(0xFF4F46E5), // ¡Texto oscuro!
                    onClick = { navController.navigate("pomodoro") }
                )

                // 4. Botón HABITOS (Morado-a-Rosa)
                GradientButton(
                    text = "HABITOS",
                    gradientColors = listOf(Color(0xFF8A2387), Color(0xFFE94057)), // Morado a Rosa
                    contentColor = Color.White,
                    onClick = { navController.navigate("habitos") }
                )

            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                indicator = { },
                divider = { }
            ) {
                tabs.forEachIndexed { index, lenguaje ->
                    val isSelected = selectedTabIndex == index
                    val tabBackgroundColor by animateColorAsState(
                        targetValue = if (isSelected) blendedBackgroundColor else Color.White,
                        label = "tabBgColor"
                    )
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.background(tabBackgroundColor),
                        unselectedContentColor = Color.Unspecified,
                        selectedContentColor = Color.Unspecified,
                        content = {
                            Image(
                                painter = painterResource(id = lenguaje.iconResId),
                                contentDescription = lenguaje.nombre,
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .size(32.dp)
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(blendedBackgroundColor)
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                val selectedLanguage = tabs[selectedTabIndex]
                val userNivel = userData?.selectedNivel ?: Nivel.DESDE_CERO
                val completedCourseIds = userData?.courses?.toSet() ?: emptySet()

                val coursesToShow = MasterCourseRepository.getCoursesByLanguageAndLevel(
                    selectedLanguage,
                    userNivel
                )
                val nivelTitle = userNivel.name.replace("_", " ").lowercase()
                    .replaceFirstChar { it.titlecase() }

                Text(
                    text = "Cursos para tu nivel: $nivelTitle",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    if (coursesToShow.isEmpty()) {
                        item {
                            CourseCard(
                                title = "¡Cursos en construcción!",
                                tags = "Vuelve pronto...",
                                isCompleted = false,
                                onClick = {}
                            )
                        }
                    } else {
                        items(coursesToShow) { course ->
                            CourseCard(
                                title = course.title,
                                tags = "${course.sessions.size} Sesiones",
                                isCompleted = completedCourseIds.contains(course.id),
                                onClick = {
                                    navController.navigate("curso_detalle/${selectedLanguage.name}/${course.id}")
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Cursos Recomendadas... ¡con amigos!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        CourseCard(
                            title = "${selectedLanguage.nombre} Básico (con Amigos)",
                            tags = "2 Sesiones | 2 Participantes",
                            isCompleted = false,
                            onClick = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBotCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF00C896),
        contentColor = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(2.dp, Color(0xFF008C69)),
        modifier = Modifier
            .height(64.dp)
            .width(300.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Pídele ayuda al ChatBot",
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- ¡¡COMPOSABLE DE BOTÓN TOTALMENTE REHECHO!! ---
@Composable
private fun GradientButton(
    text: String,
    gradientColors: List<Color>,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            // 1. Aplicamos la sombra
            .shadow(elevation = 4.dp, shape = shape)
            // 2. Aplicamos el fondo con gradiente
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = shape
            )
            // 3. Cortamos la forma (para la onda 'ripple' del click)
            .clip(shape)
            // 4. Hacemos que sea 'clicable'
            .clickable(onClick = onClick),
        // 5. Centramos el texto
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
// --- ¡¡FIN DEL COMPOSABLE MODIFICADO!! ---


@Composable
private fun CourseCard(
    title: String,
    tags: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1.0f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 2,
                    modifier = Modifier.heightIn(min = 36.dp)
                )
                Text(
                    text = tags,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            if (isCompleted) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completado",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.align(Alignment.Top)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuPrincipalPreview() {
    val fakeUserData = UserData(
        uid = "12345",
        email = "preview@uflow.com",
        usuario = "previewUser",
        selectedLanguage = Lenguaje.KOTLIN,
        selectedNivel = Nivel.DESDE_CERO,
        courses = listOf()
    )

    val fakeAuthState = AuthUiState(
        isInitializing = false,
        isLoading = false,
        userData = fakeUserData
    )

    MaterialTheme {
        PantallaMenuPrincipalUI(
            navController = rememberNavController(),
            authState = fakeAuthState,
            onLogout = {}
        )
    }
}