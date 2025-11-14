package me.uflow.unab.edu.uflow.data.model

import com.google.gson.annotations.SerializedName
import me.uflow.unab.edu.uflow.util.Lenguaje 
import me.uflow.unab.edu.uflow.util.Nivel 
import java.util.UUID

// Estructura de la respuesta
data class GeminiQuizResponse(
    @SerializedName("questionText") val questionText: String,
    @SerializedName("options") val options: Map<String, String>, 
    @SerializedName("correctAnswerKey") val correctAnswerKey: String 
)

// Estructura de la pregunta

data class Question(
    val id: String = UUID.randomUUID().toString(),
    val questionText: String,
    val options: Map<String, String>,
    val correctAnswerKey: String,
    val topic: Lenguaje, // Usando Enum Lenguaje
    val difficulty: Nivel // Usando Enum Nivel
)

data class ChallengeSettings(
    val language: Lenguaje, 
    val difficulty: Nivel 
)

enum class QuizScreenType {
    SELECTION, // Pantalla de selección de lenguaje/dificultad
    QUIZ       // Pantalla de la pregunta
}

// Estado general de la UI.

data class QuizState(
    val currentScreen: QuizScreenType = QuizScreenType.SELECTION,
    val settings: ChallengeSettings? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentQuestion: Question? = null,
    val selectedOptionKey: String? = null,
    val isGameOver: Boolean = false,
    val isAnswerCorrect: Boolean? = null, // null: no respondida
    
    // ESTADOS DE LA FIREBASE/USUARIO
    val bestStreak: Int = 0,
    val bestStreakLanguage: Lenguaje? = null,
    val bestStreakDifficulty: Nivel? = null,
    val currentStreak: Int = 0, 
    val attemptsCount: Int = 0,
    val lastStreak: Int = 0,
    val lastStreakLanguage: Lenguaje? = null,
    val lastStreakDifficulty: Nivel? = null
)