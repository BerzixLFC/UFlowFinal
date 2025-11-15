package me.uflow.unab.edu.uflow.ui.Screen

import android.R.attr.icon
import android.R.id.icon
import android.util.Log
import android.content.Context
import android.webkit.ConsoleMessage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController // <-- 1. IMPORT NECESARIO
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.Util.NotificationHelper



// Colores del diseño
val TomatoRed = Color(0xFFFFE5E5)
val TomatoRedDark = Color(0xFFFFA3A3)
val BlueButton = Color(0xFF2563EB)
val GrayButton = Color(0xFFBCBCBC)
val TextGray = Color(0xFF707070)
val HeaderBlue = Color(0xFF304F60)
val InputBg = Color(0xFFF5F5F5)

enum class SessionType { WORK, SHORT_BREAK, LONG_BREAK }

data class PomodoroState(
    val timeLeftInSeconds: Long = 25 * 60,
    val totalTimeInSeconds: Long = 25 * 60,
    val isRunning: Boolean = false,
    val sessionType: SessionType = SessionType.WORK,
    val completedPomodoros: Int = 0,
    val workMinutes: Int = 25,
    val shortBreakMinutes: Int = 5,
    val longBreakMinutes: Int = 15
)

class PomodoroViewModel(private val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(PomodoroState())
    val state: StateFlow<PomodoroState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private val notificationHelper = NotificationHelper(context)

    fun toggleTimer() {
        if (_state.value.isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun onTimerComplete() {
        val currentState = _state.value

        notificationHelper.showTimerCompleteNotification(currentState.sessionType)

        when (currentState.sessionType) {
            SessionType.WORK -> {
                val newCount = currentState.completedPomodoros + 1
                val nextSession = if (newCount % 4 == 0) {
                    SessionType.LONG_BREAK
                } else {
                    SessionType.SHORT_BREAK
                }
                startNextSession(nextSession, newCount)
            }

            else -> startNextSession(SessionType.WORK, currentState.completedPomodoros)
        }
    }

    fun updateWorkTime(minutes: Int) {
        val currentState = _state.value
        _state.value = currentState.copy(
            workMinutes = minutes,
            // Si estamos en sesión de trabajo y no está corriendo, actualiza el tiempo
            timeLeftInSeconds = if (currentState.sessionType == SessionType.WORK && !currentState.isRunning) {
                minutes * 60L
            } else {
                currentState.timeLeftInSeconds
            },
            totalTimeInSeconds = if (currentState.sessionType == SessionType.WORK) {
                minutes * 60L
            } else {
                currentState.totalTimeInSeconds
            }
        )
    }

    fun updateShortBreak(minutes: Int) {
        val currentState = _state.value
        _state.value = currentState.copy(
            shortBreakMinutes = minutes,
            // Si estamos en descanso corto y no está corriendo, actualiza el tiempo
            timeLeftInSeconds = if (currentState.sessionType == SessionType.SHORT_BREAK && !currentState.isRunning) {
                minutes * 60L
            } else {
                currentState.timeLeftInSeconds
            },
            totalTimeInSeconds = if (currentState.sessionType == SessionType.SHORT_BREAK) {
                minutes * 60L
            } else {
                currentState.totalTimeInSeconds
            }
        )
    }

    fun updateLongBreak(minutes: Int) {
        val currentState = _state.value
        _state.value = currentState.copy(
            longBreakMinutes = minutes,
            // Si estamos en descanso largo y no está corriendo, actualiza el tiempo
            timeLeftInSeconds = if (currentState.sessionType == SessionType.LONG_BREAK && !currentState.isRunning) {
                minutes * 60L
            } else {
                currentState.timeLeftInSeconds
            },
            totalTimeInSeconds = if (currentState.sessionType == SessionType.LONG_BREAK) {
                minutes * 60L
            } else {
                currentState.totalTimeInSeconds
            }
        )
    }

    private fun startTimer() {
        _state.value = _state.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_state.value.timeLeftInSeconds > 0 && _state.value.isRunning) {
                delay(1000L)
                _state.value = _state.value.copy(
                    timeLeftInSeconds = _state.value.timeLeftInSeconds - 1
                )
            }
            if (_state.value.timeLeftInSeconds == 0L) {
                onTimerComplete()
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _state.value = _state.value.copy(isRunning = false)
    }

    fun resetTimer() {
        timerJob?.cancel()
        val currentState = _state.value
        val timeForSession = when (currentState.sessionType) {
            SessionType.WORK -> currentState.workMinutes * 60L
            SessionType.SHORT_BREAK -> currentState.shortBreakMinutes * 60L
            SessionType.LONG_BREAK -> currentState.longBreakMinutes * 60L
        }
        _state.value = currentState.copy(
            timeLeftInSeconds = timeForSession,
            totalTimeInSeconds = timeForSession,
            isRunning = false
        )
    }


    private fun startNextSession(sessionType: SessionType, pomodoroCount: Int) {
        val currentState = _state.value
        val timeForSession = when (sessionType) {
            SessionType.WORK -> currentState.workMinutes * 60L
            SessionType.SHORT_BREAK -> currentState.shortBreakMinutes * 60L
            SessionType.LONG_BREAK -> currentState.longBreakMinutes * 60L
        }
        _state.value = currentState.copy(
            timeLeftInSeconds = timeForSession,
            totalTimeInSeconds = timeForSession,
            isRunning = false,
            sessionType = sessionType,
            completedPomodoros = pomodoroCount
        )
    }

    fun getFormattedTime(): String {
        val minutes = _state.value.timeLeftInSeconds / 60
        val seconds = _state.value.timeLeftInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun getProgress(): Float {
        val total = _state.value.totalTimeInSeconds.toFloat()
        val remaining = _state.value.timeLeftInSeconds.toFloat()
        return if (total > 0) (total - remaining) / total else 0f
    }
}

@Composable
fun PomodoroScreen(navController: NavController) {
    val context = LocalContext.current

    val viewModel = remember {
        PomodoroViewModel(context.applicationContext)
    }
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header - "Sueña, Cumple, Logra"
        Text(
            text = "Sueña, Cumple, Logra",
            modifier = Modifier
                .offset(y = 13.dp)
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 17.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = HeaderBlue
            )
        )

        // Icono placeholder
        Box(
            modifier = Modifier
                .offset(x = 5.dp, y = 4.dp)
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.botonatras),
                    contentDescription = "Botón atrás",
                    modifier = Modifier.size(45.dp),
                    tint = Color.Unspecified
                )
            }
        }


        // Círculo del tomate con temporizador
        Box(
            modifier = Modifier
                .offset(x = 30.dp, y = 93.dp)
                .size(350.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.tomate),
                contentDescription = "Tomate para tiempo",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.Center)
                    .offset(x = 2.dp)
            )
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .offset(y = 20.dp)
                    .background(Color(0xFFFFA3A3), RoundedCornerShape(20.dp))
                    .align(Alignment.Center)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                // 3. Tiempo en la mitad
                Text(
                    text = viewModel.getFormattedTime(),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = when (state.sessionType) {
                        SessionType.WORK -> "Tiempo de Trabajo"
                        SessionType.SHORT_BREAK -> "Descanso Corto"
                        SessionType.LONG_BREAK -> "Descanso Largo"
                    },
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
        // Logo placeholder
        Box(
            modifier = Modifier
                .offset(y = 62.dp)
                .fillMaxWidth()
                .height(63.dp)
                .background(Color.White, RoundedCornerShape(10.dp)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Uflow",
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.Center)
            )
        }

        // Frame decorativo
        Box(
            modifier = Modifier
                .offset(x = 127.dp, y = 225.dp)
                .width(136.dp)
                .height(119.dp)
                .background(Color.Transparent)
        )

        Text(
            text = "Tiempo de concentración",
            modifier = Modifier.offset(x = 50.dp, y = 383.dp), //y=317 +66
            fontSize = 20.sp,
            color = Color.Black
        )

// Input tiempo de trabajo 
        Box(
            modifier = Modifier
                .offset(x = 50.dp, y = 413.dp)
                .width(294.dp)
                .height(34.dp)
                .background(InputBg, RoundedCornerShape(9.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(9.dp))
                .padding(horizontal = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BasicTextField(
                    value = (state.timeLeftInSeconds / 60).toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { value ->
                            viewModel.updateWorkTime(value)
                        }
                    },
                    modifier = Modifier.width(40.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "minutos",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.36f)
                )
            }
        }

// "Duración de descanso"
        Text(
            text = "Duración de descanso",
            modifier = Modifier.offset(x = 50.dp, y = 464.dp),
            fontSize = 20.sp,
            color = Color.Black
        )

// Input descanso corto
        Box(
            modifier = Modifier
                .offset(x = 50.dp, y = 494.dp)
                .width(294.dp)
                .height(34.dp)
                .background(InputBg, RoundedCornerShape(9.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(9.dp))
                .padding(horizontal = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BasicTextField(
                    value = state.shortBreakMinutes.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { value ->
                            viewModel.updateShortBreak(value)
                        }
                    },
                    modifier = Modifier.width(40.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "minutos",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.36f)
                )
            }
        }

// "Duración de descanso largo"
        Text(
            text = "Duración de descanso largo",
            modifier = Modifier.offset(x = 50.dp, y = 543.dp),
            fontSize = 20.sp,
            color = Color.Black
        )

// Input descanso largo
        Box(
            modifier = Modifier
                .offset(x = 50.dp, y = 575.dp)
                .width(294.dp)
                .height(34.dp)
                .background(InputBg, RoundedCornerShape(9.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(9.dp))
                .padding(horizontal = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BasicTextField(
                    value = state.longBreakMinutes.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { value ->
                            viewModel.updateLongBreak(value)
                        }
                    },
                    modifier = Modifier.width(40.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "minutos",
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = 0.36f)
                )
            }
        }
        Box(
            modifier = Modifier
                .offset(x = 135.dp, y = 625.dp)
                .width(123.dp)
                .height(39.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp))
                .background(GrayButton, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🍅 × ${state.completedPomodoros}", 
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        
        Button(
            onClick = { viewModel.toggleTimer() },
            modifier = Modifier
                .offset(x = 40.dp, y = 690.dp)
                .width(310.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueButton),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(
                text = if (state.isRunning) "Pausar" else "Iniciar",
                fontSize = 35.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }

        // Botón Reset
        Button(
            onClick = { viewModel.resetTimer() },
            modifier = Modifier
                .offset(x = 150.dp, y = 760.dp)
                .width(100.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GrayButton),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Reset",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}