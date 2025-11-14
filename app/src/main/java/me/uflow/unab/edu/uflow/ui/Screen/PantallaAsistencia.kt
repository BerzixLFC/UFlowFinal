package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults 
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.data.model.gemini.GeminiChatMessage
import me.uflow.unab.edu.uflow.ui.theme.ChatColors
import me.uflow.unab.edu.uflow.viewmodel.GeminiChatViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PantallaAsistencia(
    navController: NavController,
    viewModel: GeminiChatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var userInput by remember { mutableStateOf("") }
    var isChatVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.messages.size) {
        if (isChatVisible && uiState.messages.isNotEmpty()) {
            val targetIndex = if (uiState.isLoading) uiState.messages.size else uiState.messages.size - 1
            if (targetIndex >= 0) {
                listState.animateScrollToItem(targetIndex)
            }
        }
    }

    Scaffold(
        containerColor = ChatColors.Background,
        topBar = {
            TopAppBar(
                title = { Text("Gemini Chat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = ChatColors.Text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChatColors.Background,
                    titleContentColor = ChatColors.Primary,
                    navigationIconContentColor = ChatColors.Text
                )
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {

                if (isChatVisible) {
                    ChatInputBar(
                        userInput = userInput,
                        onUserInputChange = { userInput = it },
                        onSendMessage = {
                            if (userInput.isNotBlank() && !uiState.isLoading) {
                                viewModel.sendMessage(userInput)
                                userInput = ""
                            }
                        },
                        isLoading = uiState.isLoading
                    )
                }
                
                NavigationBar(
                    containerColor = ChatColors.Background 
                ) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ChatColors.Primary,
                        selectedTextColor = ChatColors.Primary,
                        unselectedIconColor = ChatColors.Placeholder,
                        unselectedTextColor = ChatColors.Placeholder,
                        indicatorColor = ChatColors.BubbleModel 
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("menu") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                    )
                    NavigationBarItem(
                        selected = true, // Estás aquí
                        onClick = { /* Ya estás aquí */ },
                        icon = { Icon(Icons.Default.Code, contentDescription = "Chat") },
                        colors = itemColors // <-- 2. COLORES APLICADOS
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
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = isChatVisible,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "ScreenSwitch"
        ) { showChat ->
            if (!showChat) {
                InitialAssistantView(
                    onStartChat = { isChatVisible = true }
                )
            } else {
                ChatView(
                    messages = uiState.messages,
                    isLoading = uiState.isLoading,
                    listState = listState
                )
            }
        }
    }
}

//PANTALLA INICIAL
@Composable
private fun InitialAssistantView(onStartChat: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "</>",
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            color = ChatColors.Primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Powered By Gemini",
            color = ChatColors.Text,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onStartChat,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ChatColors.Primary,
                contentColor = ChatColors.Background
            )
        ) {
            Text(text = "Pregunta lo que quieras", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
//PANTALLA DE CHAT
@Composable
private fun ChatView(
    messages: List<GeminiChatMessage>,
    isLoading: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        items(messages) { message ->
            ChatMessageItem(message = message)
        }
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ChatColors.Primary, strokeWidth = 3.dp)
                }
            }
        }
    }
}

//BARRA DE ENTRADA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatInputBar(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatColors.Background)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = onUserInputChange,
            placeholder = { Text("Pregunta lo que quieras...", color = ChatColors.Placeholder) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ChatColors.Primary,
                unfocusedBorderColor = ChatColors.Placeholder,
                focusedTextColor = ChatColors.Text,
                unfocusedTextColor = ChatColors.Text,
                cursorColor = ChatColors.Primary,
                focusedContainerColor = ChatColors.BubbleModel.copy(alpha = 0.5f),
                unfocusedContainerColor = ChatColors.BubbleModel.copy(alpha = 0.5f)
            ),
            maxLines = 5
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSendMessage,
            modifier = Modifier
                .size(52.dp)
                .background(ChatColors.Primary, CircleShape),
            enabled = !isLoading && userInput.isNotBlank()
        ) {
            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = ChatColors.Background)
        }
    }
}

//MENSAJE
@Composable
private fun ChatMessageItem(message: GeminiChatMessage) {
    val backgroundColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (message.isFromUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(backgroundColor, shape)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }
    }
}