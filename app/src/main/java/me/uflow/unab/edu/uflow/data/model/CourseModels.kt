package me.uflow.unab.edu.uflow.data.model

import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

//Estado del Progreso
enum class SessionStatus {
    COMPLETED,
    IN_PROGRESS,
    LOCKED
}

// Tipos de Pasos de Lección 
sealed class LessonStep {
    abstract val id: Int
    //TEXTO
    data class Concept(
        override val id: Int,
        val title: String,
        val body: String
    ) : LessonStep()
    
    //"Completa el Espacio
    data class FillBlank(
        override val id: Int,
        val codeSnippet: String,
        val options: List<String>,
        val correctAnswerIndex: Int
    ) : LessonStep()

    //"Identifica el Error".
    data class FindError(
        override val id: Int,
        val codeLines: List<String>,
        val correctLineIndex: Int
    ) : LessonStep()

    //"Predice el Resultado"
    data class PredictResult(
        override val id: Int,
        val codeSnippet: String,
        val options: List<String>,
        val correctAnswerIndex: Int
    ) : LessonStep()
}

//Estructura de Sesión y Curso

//Define una sesión (lección) individual.
data class Session(
    val id: String,
    val title: String,
    val description: String,
    val steps: List<LessonStep>
)

//Define la plantilla de un curso completo.
data class CourseDetails(
    val id: String,
    val title: String,
    val language: Lenguaje,
    val level: Nivel,
    val sessions: List<Session>
)