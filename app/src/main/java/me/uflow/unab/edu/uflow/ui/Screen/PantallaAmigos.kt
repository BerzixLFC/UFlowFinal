package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable 
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmigosScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Todos", "Solicitudes", "Sugerencias")

    var searchQuery by remember { mutableStateOf("") }

    val authState by viewModel.uiState.collectAsState()
    val amigosState by viewModel.amigosState.collectAsState()
    val currentUserData = authState.userData

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Amigos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("menu") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {navController.navigate("asistencia") },
                    icon = { Icon(Icons.Default.Code, contentDescription = "Chat") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estás aquí */ },
                    icon = { Icon(Icons.Default.Group, contentDescription = "Amigos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.trim().length > 2) {
                        viewModel.searchUsersByUsername(it.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Buscar amigos por @usuario...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF1976D2)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFE3F2FD),
                    unfocusedContainerColor = Color(0xFFE3F2FD).copy(alpha = 0.7f),
                    focusedBorderColor = Color(0xFF1976D2),
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isNotBlank()) {
                Text(
                    text = "Resultados de búsqueda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (amigosState.isLoadingSearch) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(amigosState.searchResults) { user ->
                            val isFriend = currentUserData?.friends?.contains(user.uid) == true
                            val requestSent = currentUserData?.friendRequestsSent?.contains(user.uid) == true
                            val requestReceived = currentUserData?.friendRequestsReceived?.contains(user.uid) == true

                            SugerenciaListItem(
                                user = user,
                                isFriend = isFriend,
                                requestSent = requestSent,
                                requestReceived = requestReceived,
                                onAdd = { viewModel.sendFriendRequest(user.uid) },
                                onAccept = { viewModel.respondToFriendRequest(user.uid, true) },
                                onReject = { viewModel.respondToFriendRequest(user.uid, false) },
                                onClick = {
                                    navController.navigate("amigo_perfil/${user.uid}")
                                }
                            )
                        }
                    }
                }
            }
            else {
                PrimaryTabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.White) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                val count = when (title) {
                                    "Todos" -> amigosState.friendsList.size
                                    "Solicitudes" -> amigosState.friendRequests.size
                                    "Sugerencias" -> amigosState.suggestionsList.size
                                    else -> 0
                                }
                                Text("$title ($count)")
                            },
                            selectedContentColor = Color(0xFF5660A8),
                            unselectedContentColor = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    when (selectedTabIndex) {
                        0 -> {
                            if (amigosState.isLoadingFriends) {
                                item { CircularProgressIndicator() }
                            }
                            items(amigosState.friendsList) { user ->
                                FriendListItem(
                                    user = user,
                                    onClick = { 
                                        navController.navigate("amigo_perfil/${user.uid}")
                                    },
                                    onChatClick = { 
                                        navController.navigate("chat/${user.uid}/${user.getFullName()}")
                                    }
                                )
                            }
                        }
                        1 -> {
                            if (amigosState.isLoadingRequests) {
                                item { CircularProgressIndicator() }
                            }
                            items(amigosState.friendRequests) { user ->
                                SolicitudListItem(
                                    user = user,
                                    onAccept = { viewModel.respondToFriendRequest(user.uid, true) },
                                    onReject = { viewModel.respondToFriendRequest(user.uid, false) },
                                    // --- ¡¡AÑADIDO!! ---
                                    onClick = {
                                        navController.navigate("amigo_perfil/${user.uid}")
                                    }
                                )
                            }
                        }
                        2 -> {
                            if (amigosState.isLoadingSuggestions) {
                                item { CircularProgressIndicator() }
                            }
                            items(amigosState.suggestionsList) { user ->
                                val isFriend = currentUserData?.friends?.contains(user.uid) == true
                                val requestSent = currentUserData?.friendRequestsSent?.contains(user.uid) == true
                                val requestReceived = currentUserData?.friendRequestsReceived?.contains(user.uid) == true
                                SugerenciaListItem(
                                    user = user,
                                    isFriend = isFriend,
                                    requestSent = requestSent,
                                    requestReceived = requestReceived,
                                    onAdd = { viewModel.sendFriendRequest(user.uid) },
                                    onAccept = { viewModel.respondToFriendRequest(user.uid, true) },
                                    onReject = { viewModel.respondToFriendRequest(user.uid, false) },
                                    onClick = {
                                        navController.navigate("amigo_perfil/${user.uid}")
                                    }
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Invitaciones a Cursos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item {
                        InvitacionRecibidaItem(
                            courseName = "Kotlin Básico: Tu primera app",
                            fromUser = "David (Admin)",
                            onAccept = { /* TODO */ },
                            onReject = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}

//COMPONENTES INTERNOS

@Composable
private fun FriendListItem(
    user: UserData,
    onClick: () -> Unit,
    onChatClick: () -> Unit 
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(56.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nombre.take(1) + user.apellido.take(1),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .border(BorderStroke(2.dp, Color(0xFFF5F5F5)), CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.getFullName(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "@${user.usuario}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = onChatClick, 
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.LightGray.copy(alpha = 0.5f),
                    contentColor = Color.Gray
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chatear")
            }
        }
    }
}

@Composable
private fun SolicitudListItem(
    user: UserData,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onClick: () -> Unit 
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nombre.take(1) + user.apellido.take(1),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.getFullName(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "@${user.usuario}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            IconButton(
                onClick = onReject,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFBE9E7),
                    contentColor = Color(0xFFD32F2F)
                )
            ) {
                Icon(Icons.Default.Clear, contentDescription = "Rechazar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onAccept,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF388E3C)
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = "Aceptar")
            }
        }
    }
}

@Composable
private fun SugerenciaListItem(
    user: UserData,
    isFriend: Boolean,
    requestSent: Boolean,
    requestReceived: Boolean,
    onAdd: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onClick: () -> Unit 
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nombre.take(1) + user.apellido.take(1),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.getFullName(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "@${user.usuario}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            when {
                isFriend -> {
                    Text("Amigo", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                }
                requestSent -> {
                    Text("Solicitud Enviada", color = Color.Gray, fontSize = 12.sp)
                }
                requestReceived -> {
                    IconButton(
                        onClick = onReject,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFFBE9E7),
                            contentColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Rechazar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onAccept,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFE8F5E9),
                            contentColor = Color(0xFF388E3C)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Aceptar")
                    }
                }
                else -> {
                    IconButton(
                        onClick = onAdd,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFE3F2FD),
                            contentColor = Color(0xFF1976D2)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }
                }
            }
        }
    }
}

@Composable
private fun InvitacionRecibidaItem(
    courseName: String,
    fromUser: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = buildAnnotatedString {
                    append("¡Invitación recibida!\n")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
                    append("$fromUser te ha invitado a unirte al curso ")
                    pop()
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(courseName)
                },
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBE9E7), contentColor = Color(0xFFD32F2F))) {
                    Text("Rechazar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onAccept, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C), contentColor = Color.White)) {
                    Text("Aceptar")
                }
            }
        }
    }
}

@Composable
private fun InvitacionEnviadaItem(
    courseName: String,
    toUser: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = buildAnnotatedString {
                    append("Invitaste a ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(toUser)
                    pop()
                    pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
                    append(" a unirse al curso ")
                    pop()
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(courseName)
                },
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Esperando respuesta...",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


@Composable
private fun LanguageTag(lenguaje: Lenguaje) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = lenguaje.color.copy(alpha = 0.2f),
        contentColor = lenguaje.color
    ) {
        Text(
            text = lenguaje.nombre,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AmigosScreenPreview() {
    MaterialTheme {
        AmigosScreen(navController = rememberNavController())
    }
}