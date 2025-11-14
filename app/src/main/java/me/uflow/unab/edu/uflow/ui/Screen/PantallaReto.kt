package me.uflow.unab.edu.uflow.ui.Screen

import me.uflow.unab.edu.uflow.data.model.ChallengeSettings
import me.uflow.unab.edu.uflow.data.model.QuizScreenType
import me.uflow.unab.edu.uflow.data.model.QuizState
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel
import me.uflow.unab.edu.uflow.viewmodel.QuizViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel

// Extensión para Enums para facilitar la visualización
fun Nivel.toDisplayString(): String {
    return this.displayName
}

fun Lenguaje.toDisplayString(): String = this.nombre

//Constantes de Estilo

val DarkBackground = Color(0xFF121212)
val PrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF7B61FF), Color(0xFF4C75D8))
)

//Componente Raíz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeApp(
    navController: NavController,
    viewModel: QuizViewModel,
    authViewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsState()

    val friendsBestStreak by produceState<Pair<Int, String>?>(
        initialValue = null,
        key1 = authViewModel.uiState.value.userData?.friends
    ) {
        value = authViewModel.getFriendsBestStreak()
    }

    //AÑADIDO SCAFFOLD CON TOPAPPBAR
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reto UFlow", color = Color.White) },
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
                    containerColor = DarkBackground
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(DarkBackground)
        ) {
            when (state.currentScreen) {
                QuizScreenType.SELECTION -> ChallengeSelectionScreen(
                    state = state,
                    friendsBestStreak = friendsBestStreak,
                    onStartChallenge = viewModel::startChallenge,
                    modifier = Modifier.align(Alignment.Center)
                )

                QuizScreenType.QUIZ -> QuizScreen(
                    state = state,
                    onOptionSelected = viewModel::selectOption,
                    onConfirmSelection = viewModel::confirmSelection,
                    onEndAttempt = {
                        viewModel.resetChallenge()
                        navController.popBackStack()
                    }
                )
            }

            if (state.isGameOver) {
                GameOverDialog(
                    finalStreak = state.lastStreak,
                    onContinue = {
                        viewModel.resetChallenge()
                    }
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF7B61FF))
                }
            }

            state.error?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(
                                "Cerrar",
                                color = Color.White
                            )
                        }
                    }
                ) {
                    Text("Error: $it", color = Color.White)
                }
            }
        }
    }
}

//VISTA 1: Pantalla de Selección

@Composable
fun ChallengeSelectionScreen(
    state: QuizState,
    friendsBestStreak: Pair<Int, String>?,
    onStartChallenge: (ChallengeSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember { mutableStateOf(state.lastStreakLanguage ?: Lenguaje.KOTLIN) }
    var selectedDifficulty by remember {
        mutableStateOf(
            state.lastStreakDifficulty ?: Nivel.PRINCIPIANTE
        )
    }

    val languageOptions = Lenguaje.entries.toList()
    val difficultyOptions = Nivel.entries.toList()

    LaunchedEffect(state.lastStreakLanguage, state.lastStreakDifficulty) {
        selectedLanguage = state.lastStreakLanguage ?: Lenguaje.KOTLIN
        selectedDifficulty = state.lastStreakDifficulty ?: Nivel.PRINCIPIANTE
    }

    Column(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Stats Header
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tus Stats:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            StatRowItem(
                label = "Mejor Racha:",
                value = "${state.bestStreak} Aciertos. ${state.bestStreakLanguage?.nombre ?: ""} (${state.bestStreakDifficulty?.displayName ?: ""})"
            )
            StatRowItem(
                label = "Última racha:",
                value = "${state.lastStreak} Aciertos. ${state.lastStreakLanguage?.nombre ?: ""} (${state.lastStreakDifficulty?.displayName ?: ""})"
            )
            StatRowItem(
                label = "Nº Intentos:",
                value = "${state.attemptsCount} Intentos"
            )
            StatRowItem(
                label = "Mejor racha (Amigos):",
                value = friendsBestStreak?.let { "${it.first} Aciertos - ${it.second}" } ?: "N/A"
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
        }

        Text(
            text = "¿Estás preparado para el desafío?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Escoge un lenguaje:",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White.copy(alpha = 0.8f)),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val columns = 2
            val rows = (languageOptions.size + columns - 1) / columns
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(rows) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(columns) { colIndex ->
                            val index = rowIndex * columns + colIndex
                            if (index < languageOptions.size) {
                                val lang = languageOptions[index]
                                LanguageOptionButton(
                                    text = lang.nombre,
                                    isSelected = lang == selectedLanguage,
                                    color = lang.color,
                                    onClick = { selectedLanguage = lang },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        //SELECTOR DE DIFICULTAD
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Escoge tu dificultad:",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White.copy(alpha = 0.8f)),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val columns = 2
            val rows = (difficultyOptions.size + columns - 1) / columns
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(rows) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(columns) { colIndex ->
                            val index = rowIndex * columns + colIndex
                            if (index < difficultyOptions.size) {
                                val diff = difficultyOptions[index]
                                DifficultyOptionButton(
                                    text = diff.displayName,
                                    isSelected = diff == selectedDifficulty,
                                    onClick = { selectedDifficulty = diff },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        GradientButton(
            text = "Comenzar el Intento",
            enabled = !state.isLoading,
            gradient = PrimaryButtonGradient,
            onClick = {
                onStartChallenge(ChallengeSettings(selectedLanguage, selectedDifficulty))
            }
        )
    }
}

@Composable
fun StatRowItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )
    }
}

//BOTÓN PARA LENGUAJES 
@Composable
fun LanguageOptionButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonGradient = if (isSelected) {
        Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.7f)))
    } else {
        Brush.horizontalGradient(listOf(Color(0xFF333333), Color(0xFF222222))) // Gris oscuro
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .background(buttonGradient, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

//BOTÓN PARA DIFICULTAD
@Composable
fun DifficultyOptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonGradient = if (isSelected) {
        Brush.horizontalGradient(listOf(Color(0xFFE5B865), Color(0xFFF0C850))) // Amarillo/Naranja
    } else {
        Brush.horizontalGradient(listOf(Color(0xFF333333), Color(0xFF222222))) // Gris oscuro
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .background(buttonGradient, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) Color.Black else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    gradient: Brush,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val currentGradient = if (enabled) gradient else Brush.linearGradient(
        listOf(
            Color(0xFF555555),
            Color(0xFF333333)
        )
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                brush = currentGradient,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

//VISTA 2: Pantalla del Quiz

@Composable
fun QuizScreen(
    state: QuizState,
    onOptionSelected: (String) -> Unit,
    onConfirmSelection: () -> Unit,
    onEndAttempt: () -> Unit
) {
    val question = state.currentQuestion
    if (question == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando pregunta...", color = Color.White)
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .background(DarkBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            text = "Terminar el Intento",
            onClick = onEndAttempt,
            gradient = PrimaryButtonGradient,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(label = "Mejor Racha", value = state.bestStreak.toString())
            StatItem(label = "Racha Actual", value = state.currentStreak.toString())
            StatItem(label = "Intento N°", value = state.attemptsCount.toString())
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                lineHeight = 32.sp
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        question.options.entries.forEach { (key, value) ->
            QuestionOption(
                key = key,
                text = value,
                isSelected = state.selectedOptionKey == key,
                isAnswerCorrect = state.isAnswerCorrect,
                correctKey = question.correctAnswerKey,
                onClick = {
                    if (state.isAnswerCorrect == null) {
                        onOptionSelected(key)
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        GradientButton(
            text = "Confirmar Selección",
            enabled = state.selectedOptionKey != null && state.isAnswerCorrect == null && !state.isLoading,
            gradient = PrimaryButtonGradient,
            onClick = onConfirmSelection
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${question.topic.toDisplayString()} - ${question.difficulty.toDisplayString()}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Powered by",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Gemini",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun QuestionOption(
    key: String,
    text: String,
    isSelected: Boolean,
    isAnswerCorrect: Boolean?,
    correctKey: String,
    onClick: () -> Unit
) {
    val containerColor = when (isAnswerCorrect) {
        true -> if (isSelected) Color(0xFF4CAF50) else Color(0xFF333333)
        false -> {
            if (isSelected) Color(0xFFF44336)
            else if (key == correctKey) Color(0xFF4CAF50)
            else Color(0xFF333333)
        }

        null -> if (isSelected) Color(0xFF444444) else Color(0xFF333333)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .background(containerColor, RoundedCornerShape(12.dp))
            .clickable(enabled = isAnswerCorrect == null, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$key)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
            ),
            modifier = Modifier.width(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f))
        )
    }
}

//VISTA 3: Diálogo de Game Over

@Composable
fun GameOverDialog(
    finalStreak: Int,
    onContinue: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        AlertDialog(
            onDismissRequest = { /* Bloqueado */ },
            shape = RoundedCornerShape(20.dp),
            containerColor = DarkBackground.copy(alpha = 0.95f),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¡Incorrecto!",
                        color = Color(0xFFFFCC00),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "más suerte para la próxima",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Racha Final:",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$finalStreak",
                        color = Color(0xFFFFCC00),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Aciertos",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                GradientButton(
                    text = "Click para continuar",
                    onClick = onContinue,
                    gradient = PrimaryButtonGradient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }
        )
    }
}