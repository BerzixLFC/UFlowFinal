package me.uflow.unab.edu.uflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.uflow.unab.edu.uflow.data.model.Session
import me.uflow.unab.edu.uflow.data.model.SessionStatus
import me.uflow.unab.edu.uflow.data.repository.MasterCourseRepository
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

data class CursoUiState(
    val courseTitle: String = "Cargando...",
    val courseSubtitle: String = "",
    val sessionsWithStatus: List<Pair<Session, SessionStatus>> = emptyList(),
    val progressFloat: Float = 0.0f,
    val progressText: String = "0/0"
)

class CursoViewModel(
    courseId: String,
    val language: Lenguaje,
    progressMap: Map<String, String>
) : ViewModel() {

    private val _uiState = MutableStateFlow(CursoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCourse(courseId, progressMap)
    }

    private fun loadCourse(courseId: String, progressMap: Map<String, String>) {
        val courseTemplate = MasterCourseRepository.getCourseDetails(courseId)
        if (courseTemplate != null) {
            val sessionsWithStatus = mutableListOf<Pair<Session, SessionStatus>>()
            var previousStatus = SessionStatus.COMPLETED
            for (session in courseTemplate.sessions) {
                val savedStatusString = progressMap[session.id]

                val currentStatus = when {
                    savedStatusString == SessionStatus.COMPLETED.name -> SessionStatus.COMPLETED
                    savedStatusString == SessionStatus.IN_PROGRESS.name -> SessionStatus.IN_PROGRESS
                    previousStatus == SessionStatus.COMPLETED -> SessionStatus.IN_PROGRESS
                    else -> SessionStatus.LOCKED
                }
                sessionsWithStatus.add(session to currentStatus)
                previousStatus = currentStatus
            }

            val totalSessions = sessionsWithStatus.size
            val completedSessions =
                sessionsWithStatus.count { it.second == SessionStatus.COMPLETED }

            val progressFloat =
                if (totalSessions > 0) completedSessions.toFloat() / totalSessions.toFloat() else 0.0f
            val progressText = "$completedSessions/$totalSessions"
            val subtitle = "${language.nombre} - ${
                courseTemplate.level.name.lowercase().replaceFirstChar { it.titlecase() }
            }"

            _uiState.value = CursoUiState(
                courseTitle = courseTemplate.title,
                courseSubtitle = subtitle,
                sessionsWithStatus = sessionsWithStatus,
                progressFloat = progressFloat,
                progressText = progressText
            )
        }
    }
}

class CursoViewModelFactory(
    private val courseId: String,
    private val lenguajeName: String,
    private val progressMap: Map<String, String>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CursoViewModel::class.java)) {
            val language = Lenguaje.entries.find { it.name == lenguajeName } ?: Lenguaje.KOTLIN
            @Suppress("UNCHECKED_CAST")
            return CursoViewModel(courseId, language, progressMap) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}