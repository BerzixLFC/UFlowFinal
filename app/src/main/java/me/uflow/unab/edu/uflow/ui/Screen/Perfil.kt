package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

@Composable
fun PantallaPerfil(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val authState by viewModel.uiState.collectAsState()

    PantallaPerfilUI(
        navController = navController,
        userData = authState.userData,
        isMyProfile = true,
        onLogout = { viewModel.logout() },
        onChat = {},
        onRemoveFriend = {},
        onNavigateToAllCourses = {
            navController.navigate("cursos_completados")
        },
        onNavigateToEditConnections = {
            navController.navigate("editar_conexiones")
        },
        onNavigateToEditInfo = {
            navController.navigate("editar_informacion")
        },
        onNavigateToEditInterests = {
            navController.navigate("editar_intereses")
        },
        relationshipStatus = null,
        onAddFriend = {},
        onAcceptFriend = {},
        onRejectFriend = {},
        onCancelRequest = {}
    )
}

//Composable de UI reutilizable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfilUI(
    navController: NavController,
    userData: UserData?,
    isMyProfile: Boolean,
    onLogout: () -> Unit,
    onChat: () -> Unit,
    onRemoveFriend: () -> Unit,
    relationshipStatus: String?,
    onAddFriend: () -> Unit,
    onAcceptFriend: () -> Unit,
    onRejectFriend: () -> Unit,
    onCancelRequest: () -> Unit,
    onNavigateToAllCourses: () -> Unit,
    onNavigateToEditConnections: () -> Unit,
    onNavigateToEditInfo: () -> Unit,
    onNavigateToEditInterests: () -> Unit
) {
    val (rangoTexto, rangoColor) = if (userData?.selectedLanguage != null && userData.selectedNivel != null) {
        val nivelStr = userData.selectedNivel.name
            .replace("_", " ")
            .lowercase()
            .replaceFirstChar { it.titlecase() }
        val langStr = userData.selectedLanguage.nombre
        "$nivelStr en $langStr" to userData.selectedLanguage.color
    } else if (userData?.selectedLanguage != null) {
        "Aprendiz de ${userData.selectedLanguage.nombre}" to userData.selectedLanguage.color
    } else {
        "Explorador" to Color.Gray
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            ProfileTopBar(
                isMyProfile = isMyProfile,
                onLogout = onLogout,
                onChat = onChat,
                onRemoveFriend = onRemoveFriend,
                onBack = if (!isMyProfile) {
                    { navController.popBackStack() }
                } else null,
                relationshipStatus = relationshipStatus,
                onAddFriend = onAddFriend,
                onAcceptFriend = onAcceptFriend,
                onRejectFriend = onRejectFriend,
                onCancelRequest = onCancelRequest
            )
        },
        bottomBar = {
            if (isMyProfile) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("menu") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("asistencia") },
                        icon = { Icon(Icons.Default.Code, contentDescription = "Chat") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("amigos") },
                        icon = { Icon(Icons.Default.Group, contentDescription = "Amigos") }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { /* Ya estás aquí */ },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {

            //ENCABEZADO Y FOTO DE PERFIL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0C0D12)),
                contentAlignment = Alignment.Center
            ) {
                // Foto de perfil
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 60.dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD9D9D9))
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "FOTO",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                    if (isMyProfile) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { /*TODO: Editar foto*/ },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "Editar Avatar",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            //INFORMACIÓN DEL USUARIO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(72.dp))
                Text(
                    text = userData?.usuario?.takeIf { it.isNotBlank() } ?: "Usuario",
                    color = Color(0xFF1F2937),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = rangoColor,
                    shape = RoundedCornerShape(16.dp),
                    contentColor = Color.White
                ) {
                    Text(
                        text = rangoTexto,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // SECCIONES DE CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //Intereses
                ProfileSection(
                    title = "Intereses",
                    containerColor = Color(0xFFF3F4F6),
                    onEditClick = if (isMyProfile) onNavigateToEditInterests else null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val intereses = userData?.intereses ?: emptyList()

                            if (intereses.isEmpty()) {
                                Text(
                                    text = if (isMyProfile) "Añade tus intereses..." else "Sin intereses",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            } else {
                                intereses.forEach { nombre ->
                                    val color = when (nombre) {
                                        "Kotlin" -> Lenguaje.KOTLIN.color
                                        "Java" -> Lenguaje.JAVA.color
                                        "Desarrollo Web" -> Lenguaje.WEB.color
                                        "Python" -> Lenguaje.PYTHON.color
                                        else -> Color(0xFFE5E7EB)
                                    }
                                    InterestChip(nombre, color)
                                }
                            }
                        }
                    }
                }

                //Información
                ProfileSection(
                    title = "Información",
                    onEditClick = if (isMyProfile) onNavigateToEditInfo else null
                ) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        InfoRow(
                            "Nombre:",
                            userData?.getFullName()?.takeIf { it.isNotBlank() }
                                ?: "Nombre Apellido")
                        InfoRowDivider()
                        InfoRow("Correo:", userData?.email ?: "Oculto")
                        InfoRowDivider()
                        InfoRow(
                            "Cumpleaños:",
                            userData?.cumpleanos?.takeIf { it.isNotBlank() } ?: "Oculto")
                        InfoRowDivider()
                        InfoRow(
                            "Localización:",
                            userData?.localizacion?.takeIf { it.isNotBlank() } ?: "No especificada")
                    }
                }

                //Conexiones
                ProfileSection(
                    title = "Conexiones",
                    onEditClick = if (isMyProfile) onNavigateToEditConnections else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .heightIn(min = 40.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
                    ) {
                        val handles = listOf(
                            Triple(userData?.instagramHandle, "Instagram", R.drawable.ins),
                            Triple(userData?.twitterHandle, "Twitter", R.drawable.x),
                            Triple(userData?.githubHandle, "GitHub", R.drawable.github)
                        ).filter { !it.first.isNullOrBlank() }

                        if (handles.isEmpty()) {
                            Text(
                                text = if (isMyProfile) "Añade tus conexiones..." else "Sin conexiones",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        } else {
                            handles.forEach { (handle, text, iconRes) ->
                                val color = when (text) {
                                    "Instagram" -> Color(0xFFE4405F)
                                    "Twitter" -> Color(0xFF000000)
                                    "GitHub" -> Color(0xFF333333)
                                    else -> Color.Gray
                                }
                                SocialButton(
                                    text = text,
                                    iconRes = iconRes,
                                    color = color,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Cursos Terminados
                ProfileSection(title = "Cursos Terminados") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val cursosCompletados =
                            ((userData?.completedCourses ?: emptyList()) + (userData?.courses
                                ?: emptyList()))
                                .toSet().toList()

                        val cursoNombres = mapOf(
                            "kotlin_intro" to "Bienvenido a Kotlin",
                            "kotlin_cero" to "Kotlin desde Cero",
                            "kotlin_cero_2" to "Kotlin: Siguientes Pasos",
                            "kotlin_cero_3" to "Kotlin: Conceptos Básicos",
                            "kotlin_cero_4" to "Kotlin: Fundamentos de Datos",
                            "kotlin_cero_5" to "Kotlin: Mini-Proyectos",
                            "kotlin_principiante" to "Kotlin Principiante: POO",
                            "kotlin_intermedio" to "Kotlin Intermedio: Funcional",
                            "kotlin_avanzado" to "Kotlin Avanzado: Asincronía"
                        )

                        val cursosAMostrar = cursosCompletados
                            .mapNotNull { id -> cursoNombres[id]?.let { nombre -> id to nombre } }
                            .take(3)

                        if (cursosAMostrar.isEmpty()) {
                            Text(
                                "Aún no has completado ningún curso.",
                                modifier = Modifier.padding(8.dp),
                                color = Color.Gray
                            )
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                cursosAMostrar.forEach { (id, nombre) ->
                                    CompletedCourseCard(
                                        title = nombre,
                                        language = Lenguaje.KOTLIN
                                    )
                                }
                            }
                        }

                        if (cursosCompletados.size > 3) {
                            TextButton(onClick = onNavigateToAllCourses) {
                                Text("Ver todo (${cursosCompletados.size})...")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    isMyProfile: Boolean,
    onLogout: () -> Unit,
    onChat: () -> Unit,
    onRemoveFriend: () -> Unit,
    onBack: (() -> Unit)? = null,
    relationshipStatus: String?,
    onAddFriend: () -> Unit,
    onAcceptFriend: () -> Unit,
    onRejectFriend: () -> Unit,
    onCancelRequest: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { /* Título vacío */ },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0C0D12),
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = {
            if (isMyProfile) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ajustes") },
                            leadingIcon = { Icon(Icons.Default.Settings, null) },
                            onClick = {
                                showMenu = false
                                // TODO: 
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                            onClick = {
                                showMenu = false
                                onLogout()
                            }
                        )
                    }
                }
            } else {
                //PERFIL DE OTRO USUARIO
                when (relationshipStatus) {
                    "FRIEND" -> {
                        IconButton(onClick = onChat) {
                            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Abrir Chat")
                        }
                        IconButton(onClick = onRemoveFriend) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar Amigo",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                    "SENT" -> {
                        OutlinedButton(
                            onClick = onCancelRequest,
                            modifier = Modifier.padding(end = 8.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text("Solicitud Enviada", color = Color.White, fontSize = 12.sp)
                        }
                    }
                    "RECEIVED" -> {
                        IconButton(onClick = onRejectFriend, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.Default.Clear, "Rechazar", tint = Color.Red)
                        }
                        IconButton(onClick = onAcceptFriend, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.Default.Check, "Aceptar", tint = Color(0xFF4CAF50))
                        }
                    }
                    "NONE" -> {
                        IconButton(onClick = onAddFriend) {
                            Icon(Icons.Default.PersonAdd, "Agregar Amigo", tint = Color.White)
                        }
                    }
                    else -> {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun ProfileSection(
    title: String,
    containerColor: Color = Color(0xFFF8F9FC),
    onEditClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black,
            )
            onEditClick?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar $title",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            content = content
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF1F2937)
        )
        Text(
            text = value,
            fontWeight = if (value == "Oculto") FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp,
            color = if (value == "Oculto") Color.Gray else Color(0xFF374151),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun InfoRowDivider() {
    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 1.dp)
}

@Composable
private fun InterestChip(
    text: String,
    color: Color
) {
    val contentColor = if (color == Color(0xFFE5E7EB)) Color(0xFF1F2937) else Color.White
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Composable
private fun SocialButton(
    text: String,
    iconRes: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /*TODO: Abrir URL*/ },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(40.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CompletedCourseCard(
    title: String,
    language: Lenguaje
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp),
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
                maxLines = 2,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.align(Alignment.BottomEnd)
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPerfilPreview() {
    val fakeUserData = UserData(
        uid = "123",
        email = "crojaslfc@gmail.com",
        usuario = "crojaslfc",
        nombre = "Carlos",
        apellido = "Rojas",
        selectedLanguage = Lenguaje.JAVA,
        completedCourses = listOf("kotlin_intro", "kotlin_cero"),
        instagramHandle = "carlos",
        twitterHandle = "carlos",
        intereses = listOf("Kotlin", "Java", "Desarrollo Web")
    )
    MaterialTheme {
        PantallaPerfilUI(
            navController = rememberNavController(),
            userData = fakeUserData,
            isMyProfile = true,
            onLogout = {},
            onChat = {},
            onRemoveFriend = {},
            onNavigateToAllCourses = {},
            onNavigateToEditConnections = {},
            onNavigateToEditInfo = {},
            onNavigateToEditInterests = {},
            relationshipStatus = "FRIEND",
            onAddFriend = {},
            onAcceptFriend = {},
            onRejectFriend = {},
            onCancelRequest = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPerfilAmigoPreview() {
    val fakeUserData = UserData(
        uid = "456",
        email = "amigo@gmail.com",
        usuario = "amigo123",
        nombre = "Juan",
        apellido = "Perez",
        selectedLanguage = Lenguaje.PYTHON,
        completedCourses = listOf("kotlin_intro"),
        githubHandle = "juanp",
        intereses = listOf("Python", "IA")
    )
    MaterialTheme {
        PantallaPerfilUI(
            navController = rememberNavController(),
            userData = fakeUserData,
            isMyProfile = false,
            onLogout = {},
            onChat = {},
            onRemoveFriend = {},
            onNavigateToAllCourses = {},
            onNavigateToEditConnections = {},
            onNavigateToEditInfo = {},
            onNavigateToEditInterests = {},
            relationshipStatus = "NONE", //Preview como no AmigO
            onAddFriend = {},
            onAcceptFriend = {},
            onRejectFriend = {},
            onCancelRequest = {}
        )
    }
}