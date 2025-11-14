package me.uflow.unab.edu.uflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.uflow.unab.edu.uflow.data.model.ChallengeSettings
import me.uflow.unab.edu.uflow.data.model.GeminiQuizResponse
import me.uflow.unab.edu.uflow.data.model.Question
import me.uflow.unab.edu.uflow.data.model.QuizScreenType
import me.uflow.unab.edu.uflow.data.model.QuizState
import me.uflow.unab.edu.uflow.data.repository.UserRepository
import me.uflow.unab.edu.uflow.data.repository.UserRepositoryImpl
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel
import kotlin.random.Random

object GeminiApiSimulated {
    private const val API_KEY = "TU_CLAVE_AQUI"

    suspend fun generateQuestionFromApi(language: Lenguaje, difficulty: Nivel): GeminiQuizResponse =
        withContext(Dispatchers.IO) {
            val difficultyDisplay = difficulty.name.lowercase().replace("_", " ")
            val userQuery =
                "Genera una pregunta de opción múltiple para un quiz sobre el tema: ${language.nombre}. La dificultad debe ser: $difficultyDisplay."

            val correctKey = listOf("A", "B", "C", "D").random()
            val mockQuestionText = when (language) {
                Lenguaje.JAVA -> "¿Cuál de las siguientes afirmaciones sobre la palabra clave 'static' en Java es correcta?"
                Lenguaje.KOTLIN -> "¿Qué palabra clave se usa para declarar variables mutables en Kotlin?"
                else -> "$userQuery"
            }

            val mockOptions = when (language) {
                Lenguaje.JAVA -> mapOf(
                    "A" to "Los métodos static pueden acceder a variables de instancia no estáticas.",
                    "B" to "Una variable static se comparte entre todas las instancias de una clase, y existe incluso si no se ha creado ningún objeto.",
                    "C" to "Las clases static pueden heredar.",
                    "D" to "El bloque static se ejecuta cada vez que se crea una nueva instancia de la clase."
                )

                Lenguaje.KOTLIN -> mapOf(
                    "A" to "val",
                    "B" to "var",
                    "C" to "const",
                    "D" to "let"
                )

                else -> mapOf(
                    "A" to "Opción A: Prueba de acierto.",
                    "B" to "Opción B: Prueba de fallo.",
                    "C" to "Opción C: Otra opción.",
                    "D" to "Opción D: Última opción."
                )
            }

            val mockResponseJson = Gson().toJson(
                GeminiQuizResponse(
                    questionText = mockQuestionText,
                    options = mockOptions,
                    correctAnswerKey = if (language == Lenguaje.JAVA || language == Lenguaje.KOTLIN) "B" else correctKey
                )
            )

            kotlinx.coroutines.delay(Random.nextLong(1000, 2500))

            return@withContext Gson().fromJson(mockResponseJson, GeminiQuizResponse::class.java)
        }
}

class QuizViewModel : ViewModel() {
    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private val auth: FirebaseAuth
    private val userRepository: UserRepository
    private var currentUid: String? = null

    init {
        auth = Firebase.auth
        userRepository = UserRepositoryImpl(auth)
        viewModelScope.launch {
            userRepository.userDataStream.collect { userData ->
                if (userData != null) {
                    currentUid = userData.uid
                    _state.update {
                        it.copy(
                            bestStreak = userData.bestStreak,
                            bestStreakLanguage = userData.bestStreakLanguage,
                            bestStreakDifficulty = userData.bestStreakDifficulty,
                            attemptsCount = userData.attemptsCount,
                            lastStreak = userData.lastStreak,
                            lastStreakLanguage = userData.lastStreakLanguage,
                            lastStreakDifficulty = userData.lastStreakDifficulty
                        )
                    }
                } else {
                    currentUid = null
                    _state.update {
                        it.copy(
                            bestStreak = 0,
                            bestStreakLanguage = null,
                            bestStreakDifficulty = null,
                            attemptsCount = 0,
                            lastStreak = 0,
                            lastStreakLanguage = null,
                            lastStreakDifficulty = null,
                            currentStreak = 0
                        )
                    }
                }
            }
        }
    }

    //Lógica del Juego

    fun startChallenge(settings: ChallengeSettings) {
        val currentState = _state.value
        val newAttemptsCount = currentState.attemptsCount + 1

        _state.update {
            it.copy(
                settings = settings,
                attemptsCount = newAttemptsCount,
                currentStreak = 0,
                isGameOver = false,
                isAnswerCorrect = null,
                selectedOptionKey = null,
                lastStreak = if (currentState.currentStreak > 0) currentState.currentStreak else currentState.lastStreak,
                lastStreakLanguage = if (currentState.currentStreak > 0) currentState.settings?.language else currentState.lastStreakLanguage,
                lastStreakDifficulty = if (currentState.currentStreak > 0) currentState.settings?.difficulty else currentState.lastStreakDifficulty
            )
        }

        // Guardar el nuevo intento y la última racha en Firebase
        saveStats(
            newBestStreak = currentState.bestStreak, 
            newBestStreakLanguage = currentState.bestStreakLanguage,
            newBestStreakDifficulty = currentState.bestStreakDifficulty,
            newAttemptsCount = newAttemptsCount, 
            newLastStreak = _state.value.lastStreak, 
            newLastStreakLanguage = _state.value.lastStreakLanguage,
            newLastStreakDifficulty = _state.value.lastStreakDifficulty
        )

        loadNewQuestion()
    }

    fun selectOption(key: String) {
        if (_state.value.isAnswerCorrect == null) {
            _state.update { it.copy(selectedOptionKey = key) }
        }
    }

    fun confirmSelection() {
        val currentState = _state.value
        if (currentState.selectedOptionKey == null || currentState.currentQuestion == null || currentState.isAnswerCorrect != null) return

        val selectedKey = currentState.selectedOptionKey
        val correctKey = currentState.currentQuestion.correctAnswerKey

        if (selectedKey == correctKey) {
            val newStreak = currentState.currentStreak + 1
            var newBest = currentState.bestStreak
            var newBestLang = currentState.bestStreakLanguage
            var newBestDiff = currentState.bestStreakDifficulty
            
            if (newStreak > currentState.bestStreak) {
                newBest = newStreak
                newBestLang = currentState.settings?.language
                newBestDiff = currentState.settings?.difficulty
            }

            _state.update {
                it.copy(
                    currentStreak = newStreak,
                    bestStreak = newBest, 
                    bestStreakLanguage = newBestLang,
                    bestStreakDifficulty = newBestDiff,
                    isAnswerCorrect = true
                )
            }
            
            if (newBest > currentState.bestStreak) {
                saveStats(
                    newBestStreak = newBest,
                    newBestStreakLanguage = newBestLang,
                    newBestStreakDifficulty = newBestDiff,
                    newAttemptsCount = currentState.attemptsCount, 
                    newLastStreak = currentState.lastStreak, 
                    newLastStreakLanguage = currentState.lastStreakLanguage,
                    newLastStreakDifficulty = currentState.lastStreakDifficulty
                )
            }

            viewModelScope.launch {
                kotlinx.coroutines.delay(1000)
                loadNewQuestion()
            }
        } else {
            _state.update {
                it.copy(
                    isAnswerCorrect = false,
                    isGameOver = true,
                    lastStreak = currentState.currentStreak, 
                    lastStreakLanguage = currentState.settings?.language,
                    lastStreakDifficulty = currentState.settings?.difficulty
                )
            }
            saveStats(
                newBestStreak = currentState.bestStreak, 
                newBestStreakLanguage = currentState.bestStreakLanguage,
                newBestStreakDifficulty = currentState.bestStreakDifficulty,
                newAttemptsCount = currentState.attemptsCount, 
                newLastStreak = currentState.currentStreak, 
                newLastStreakLanguage = currentState.settings?.language,
                newLastStreakDifficulty = currentState.settings?.difficulty
            )
        }
    }
    fun resetChallenge() {
        _state.update {
            it.copy(
                isGameOver = false,
                currentStreak = 0,
                selectedOptionKey = null,
                isAnswerCorrect = null,
                currentScreen = QuizScreenType.SELECTION, 
                currentQuestion = null,
                settings = null
            )
        }
    }
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
    private fun saveStats(
        newBestStreak: Int,
        newBestStreakLanguage: Lenguaje?,
        newBestStreakDifficulty: Nivel?,
        newAttemptsCount: Int,
        newLastStreak: Int,
        newLastStreakLanguage: Lenguaje?,
        newLastStreakDifficulty: Nivel?
    ) {
        val uid = currentUid
        if (uid == null) {
            Log.w("QuizViewModel", "No se pueden guardar stats, UID es nulo.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.updateChallengeStats(
                    uid = uid,
                    newBestStreak = newBestStreak,
                    newBestStreakLanguage = newBestStreakLanguage,
                    newBestStreakDifficulty = newBestStreakDifficulty,
                    newAttemptsCount = newAttemptsCount,
                    newLastStreak = newLastStreak,
                    newLastStreakLanguage = newLastStreakLanguage,
                    newLastStreakDifficulty = newLastStreakDifficulty
                )
                Log.d("QuizViewModel", "Estadísticas de reto guardadas en Firebase.")
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error al guardar estadísticas de reto", e)
                _state.update { it.copy(error = "No se pudieron guardar tus stats.") }
            }
        }
    }
    
    private fun loadNewQuestion() {
        val settings = _state.value.settings ?: return

        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                isAnswerCorrect = null,
                selectedOptionKey = null
            )
        }
        viewModelScope.launch {
            try {
                val geminiResponse = GeminiApiSimulated.generateQuestionFromApi(
                    language = settings.language,
                    difficulty = settings.difficulty
                )

                val newQuestion = Question(
                    questionText = geminiResponse.questionText,
                    options = geminiResponse.options,
                    correctAnswerKey = geminiResponse.correctAnswerKey,
                    topic = settings.language,
                    difficulty = settings.difficulty
                )

                _state.update {
                    it.copy(
                        currentQuestion = newQuestion,
                        currentScreen = QuizScreenType.QUIZ,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al cargar la pregunta: ${e.message}",
                        isLoading = false,
                        currentScreen = QuizScreenType.SELECTION
                    )
                }
            }
        }
    }
}