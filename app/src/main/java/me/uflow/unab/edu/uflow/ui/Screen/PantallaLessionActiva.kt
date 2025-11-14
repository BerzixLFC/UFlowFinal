package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.data.model.LessonStep
import me.uflow.unab.edu.uflow.data.model.SessionStatus
import androidx.compose.foundation.BorderStroke
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.ui.viewmodel.AnswerStatus
import me.uflow.unab.edu.uflow.ui.viewmodel.LeccionUiState
import me.uflow.unab.edu.uflow.ui.viewmodel.LeccionViewModel
import me.uflow.unab.edu.uflow.ui.viewmodel.LeccionViewModelFactory
import me.uflow.unab.edu.uflow.ui.viewmodel.LessonResult
import me.uflow.unab.edu.uflow.util.Lenguaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLeccionActiva(
    navController: NavController,
    authViewModel: AuthViewModel,
    courseId: String,
    sessionId: String,
    lenguajeName: String
) {
    val factory = LeccionViewModelFactory(courseId, sessionId)
    val viewModel: LeccionViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsState()
    val language = Lenguaje.entries.find { it.name == lenguajeName } ?: Lenguaje.KOTLIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.sessionTitle, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = language.color.copy(alpha = 0.2f)
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = !uiState.isLessonCompleted) {
                LessonBottomBar(
                    uiState = uiState,
                    onCheck = { viewModel.checkAnswer() },
                    onNext = { viewModel.nextStep() }
                )
            }
        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = uiState.isLessonCompleted,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "LessonContent"
        ) { isCompleted ->
            if (isCompleted) {
                PantallaLeccionCompletada(
                    navController = navController,
                    authViewModel = authViewModel,
                    viewModel = viewModel,
                    uiState = uiState,
                    courseId = courseId
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    LinearProgressIndicator(
                        progress = { uiState.getProgressFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = language.color,
                        trackColor = Color.Gray.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    val step = uiState.getCurrentStep()

                    if (step != null) {
                        AnimatedContent(
                            targetState = step.id,
                            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                            label = "StepContent"
                        ) { stepId ->
                            key(stepId) {
                                val currentStep = uiState.steps.find { it.id == stepId }
                                when (currentStep) {
                                    is LessonStep.Concept -> ConceptCard(currentStep)
                                    is LessonStep.FillBlank -> FillBlankQuiz(
                                        step = currentStep,
                                        uiState = uiState,
                                        onAnswerSelected = { viewModel.onAnswerSelected(it) }
                                    )
                                    is LessonStep.FindError -> FindErrorQuiz(
                                        step = currentStep,
                                        uiState = uiState,
                                        onAnswerSelected = { viewModel.onAnswerSelected(it) }
                                    )
                                    is LessonStep.PredictResult -> PredictResultQuiz(
                                        step = currentStep,
                                        uiState = uiState,
                                        onAnswerSelected = { viewModel.onAnswerSelected(it) }
                                    )
                                    null -> {}
                                }
                            }
                        }
                    } else {
                        CircularProgressIndicator()
                        Text(
                            "¡Esta lección está en construcción! Vuelve pronto.",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

//COMPONENTES DE PASOS

@Composable
fun ConceptCard(step: LessonStep.Concept) {
    Column {
        Text(
            step.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(step.body, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
    }
}

@Composable
fun FillBlankQuiz(
    step: LessonStep.FillBlank,
    uiState: LeccionUiState,
    onAnswerSelected: (Int) -> Unit
) {
    Column {
        Text(
            "Completa el código:",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        CodeSnippet(code = step.codeSnippet)
        Spacer(modifier = Modifier.height(24.dp))

        step.options.forEachIndexed { index, text ->
            QuizOption(
                text = text,
                isSelected = uiState.selectedAnswerIndex == index,
                isCorrect = uiState.answerStatus == AnswerStatus.CORRECT && uiState.selectedAnswerIndex == index,
                isIncorrect = uiState.answerStatus == AnswerStatus.INCORRECT && uiState.selectedAnswerIndex == index,
                isEnabled = uiState.answerStatus == AnswerStatus.UNANSWERED,
                onClick = { onAnswerSelected(index) }
            )
        }
    }
}

@Composable
fun FindErrorQuiz(
    step: LessonStep.FindError,
    uiState: LeccionUiState,
    onAnswerSelected: (Int) -> Unit
) {
    Column {
        Text(
            "Toca la línea con el error:",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        step.codeLines.forEachIndexed { index, line ->
            QuizOption(
                text = line,
                isCode = true,
                isSelected = uiState.selectedAnswerIndex == index,
                isCorrect = uiState.answerStatus == AnswerStatus.CORRECT && uiState.selectedAnswerIndex == index,
                isIncorrect = uiState.answerStatus == AnswerStatus.INCORRECT && uiState.selectedAnswerIndex == index,
                isEnabled = uiState.answerStatus == AnswerStatus.UNANSWERED,
                onClick = { onAnswerSelected(index) }
            )
        }
    }
}

@Composable
fun PredictResultQuiz(
    step: LessonStep.PredictResult,
    uiState: LeccionUiState,
    onAnswerSelected: (Int) -> Unit
) {
    Column {
        Text(
            "¿Qué mostrará la consola?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        CodeSnippet(code = step.codeSnippet)
        Spacer(modifier = Modifier.height(24.dp))

        step.options.forEachIndexed { index, text ->
            QuizOption(
                text = text,
                isSelected = uiState.selectedAnswerIndex == index,
                isCorrect = uiState.answerStatus == AnswerStatus.CORRECT && uiState.selectedAnswerIndex == index,
                isIncorrect = uiState.answerStatus == AnswerStatus.INCORRECT && uiState.selectedAnswerIndex == index,
                isEnabled = uiState.answerStatus == AnswerStatus.UNANSWERED,
                onClick = { onAnswerSelected(index) }
            )
        }
    }
}

//Componentes de UI reutilizables

@Composable
fun CodeSnippet(code: String) {
    Surface(
        color = Color(0xFF2B2B2B),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = code,
            color = Color(0xFFCCCCCC),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun QuizOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    isCode: Boolean = false
) {
    val borderColor = when {
        isCorrect -> Color(0xFF4CAF50)
        isIncorrect -> Color.Red
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Gray.copy(alpha = 0.5f)
    }

    val backgroundColor = when {
        isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.1f)
        isIncorrect -> Color.Red.copy(alpha = 0.1f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else -> Color.Transparent
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = isEnabled, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default,
            fontSize = if (isCode) 14.sp else 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun LessonBottomBar(
    uiState: LeccionUiState,
    onCheck: () -> Unit,
    onNext: () -> Unit
) {
    val step = uiState.getCurrentStep()
    val isQuiz = step is LessonStep.Concept == false

    val buttonText = when {
        uiState.answerStatus != AnswerStatus.UNANSWERED -> "Siguiente"
        isQuiz -> "Comprobar"
        else -> "Siguiente"
    }

    val buttonColor = when (uiState.answerStatus) {
        AnswerStatus.CORRECT -> Color(0xFF4CAF50)
        AnswerStatus.INCORRECT -> Color.Red
        AnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.primary
    }

    val buttonEnabled = when {
        uiState.answerStatus != AnswerStatus.UNANSWERED -> true
        isQuiz -> uiState.selectedAnswerIndex != null
        else -> true
    }

    Surface(shadowElevation = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = uiState.answerStatus != AnswerStatus.UNANSWERED) {
                if (step !is LessonStep.Concept) {
                    val message =
                        if (uiState.answerStatus == AnswerStatus.CORRECT) "¡Correcto!" else "¡Ups! Sigue intentando."
                    val color =
                        if (uiState.answerStatus == AnswerStatus.CORRECT) Color(0xFF4CAF50) else Color.Red
                    Text(
                        message,
                        color = color,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    when {
                        uiState.answerStatus != AnswerStatus.UNANSWERED -> onNext()
                        isQuiz -> onCheck()
                        else -> onNext()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                enabled = buttonEnabled
            ) {
                Text(buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PantallaLeccionCompletada(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: LeccionViewModel,
    uiState: LeccionUiState,
    courseId: String // 
) {
    val result = uiState.lessonResult
    val score = uiState.correctAnswerCount
    val total = uiState.totalQuestions

    val (icon, title, color) = if (result == LessonResult.PASSED) {
        Triple("🎉", "¡Felicidades, Aprobaste!", Color(0xFF4CAF50))
    } else {
        Triple("😥", "¡Sigue practicando!", Color.Red)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(icon, fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Tu puntaje: $score / $total",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (result == LessonResult.PASSED) {
                    authViewModel.updateSessionStatus(
                        viewModel.sessionId,
                        SessionStatus.COMPLETED.name
                    )
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Continuar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.resetLesson() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Refresh, "Reintentar", modifier = Modifier.padding(end = 8.dp))
            Text("Volverlo a hacer")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "¿Te quedaron dudas?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = { navController.navigate("asistente") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Chat, "IA", modifier = Modifier.padding(end = 8.dp))
            Text("Preguntar al Asistente de IA")
        }
    }
}