package me.uflow.unab.edu.uflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update 
import me.uflow.unab.edu.uflow.data.model.LessonStep
import me.uflow.unab.edu.uflow.data.repository.MasterCourseRepository

enum class AnswerStatus {
    UNANSWERED,
    CORRECT,
    INCORRECT
}
enum class LessonResult {
    PASSED,
    FAILED
}

data class LeccionUiState(
    val sessionTitle: String = "Cargando...",
    val steps: List<LessonStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val answerStatus: AnswerStatus = AnswerStatus.UNANSWERED,
    val correctAnswerCount: Int = 0,
    val totalQuestions: Int = 0,
    val isLessonCompleted: Boolean = false,
    val lessonResult: LessonResult? = null
) {
    fun getCurrentStep(): LessonStep? = steps.getOrNull(currentStepIndex)
    fun getProgressFloat(): Float = if (steps.isEmpty()) 0f else (currentStepIndex + 1).toFloat() / steps.size.toFloat()
}

class LeccionViewModel(
    private val courseId: String,
    val sessionId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeccionUiState())
    val uiState = _uiState.asStateFlow()

    private var originalSteps: List<LessonStep> = emptyList()

    init {
        loadLesson()
    }

    private fun loadLesson() {
        val session = MasterCourseRepository.getSessionDetails(courseId, sessionId)

        if (session != null) {
            originalSteps = session.steps
            val totalQuestions = session.steps.count { it !is LessonStep.Concept }

            _uiState.update {
                it.copy(
                    sessionTitle = session.title,
                    steps = session.steps,
                    totalQuestions = totalQuestions
                )
            }
        }
    }

    fun onAnswerSelected(answerIndex: Int) {
        if (_uiState.value.answerStatus == AnswerStatus.UNANSWERED) {
            _uiState.update { it.copy(selectedAnswerIndex = answerIndex) }
        }
    }

    fun checkAnswer() {
        val state = _uiState.value
        val step = state.getCurrentStep() ?: return
        if (state.answerStatus != AnswerStatus.UNANSWERED) return
        val selectedIndex = state.selectedAnswerIndex ?: return

        val isCorrect = when (step) {
            is LessonStep.FillBlank -> selectedIndex == step.correctAnswerIndex
            is LessonStep.FindError -> selectedIndex == step.correctLineIndex
            is LessonStep.PredictResult -> selectedIndex == step.correctAnswerIndex
            is LessonStep.Concept -> true
        }

        if (isCorrect) {
            val newScore = if (step !is LessonStep.Concept) {
                state.correctAnswerCount + 1
            } else {
                state.correctAnswerCount
            }

            _uiState.update { it.copy(
                answerStatus = AnswerStatus.CORRECT,
                correctAnswerCount = newScore
            )}
        } else {
            _uiState.update { it.copy(answerStatus = AnswerStatus.INCORRECT) }
        }
    }

    fun nextStep() {
        val state = _uiState.value
        if (state.currentStepIndex >= state.steps.size - 1) {
            val passingScore = (state.totalQuestions / 2) + 1

            _uiState.update {
                it.copy(
                    isLessonCompleted = true,
                    lessonResult = if (it.correctAnswerCount >= passingScore) {
                        LessonResult.PASSED
                    } else {
                        LessonResult.FAILED
                    }
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    currentStepIndex = it.currentStepIndex + 1,
                    selectedAnswerIndex = null,
                    answerStatus = AnswerStatus.UNANSWERED
                )
            }
        }
    }

    fun resetLesson() {
        _uiState.update {
            it.copy(
                sessionTitle = it.sessionTitle,
                steps = originalSteps,
                totalQuestions = it.totalQuestions,
                currentStepIndex = 0,
                selectedAnswerIndex = null,
                answerStatus = AnswerStatus.UNANSWERED,
                correctAnswerCount = 0,
                isLessonCompleted = false,
                lessonResult = null
            )
        }
    }
}

class LeccionViewModelFactory(
    private val courseId: String,
    private val sessionId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeccionViewModel(courseId, sessionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}