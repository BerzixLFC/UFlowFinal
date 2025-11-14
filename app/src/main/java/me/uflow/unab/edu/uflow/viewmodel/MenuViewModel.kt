package me.uflow.unab.edu.uflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.uflow.unab.edu.uflow.data.model.CourseDetails
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.data.repository.MasterCourseRepository
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

data class MenuUiState(
    val cursosDesdeCero: List<CourseDetails> = emptyList(),
    val cursosPrincipiante: List<CourseDetails> = emptyList(),
    val cursosIntermedio: List<CourseDetails> = emptyList(),
    val cursosAvanzado: List<CourseDetails> = emptyList(),
    val nivelSugerido: Nivel?,
    val lenguajeSeleccionado: Lenguaje?,
    val cursosCompletadosIds: List<String> = emptyList()
)

class MenuViewModel(
    userData: UserData?
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState(
        nivelSugerido = userData?.selectedNivel,
        lenguajeSeleccionado = userData?.selectedLanguage,
        cursosCompletadosIds = userData?.courses ?: emptyList()
    ))
    val uiState = _uiState.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        val language = _uiState.value.lenguajeSeleccionado ?: return
        _uiState.value = _uiState.value.copy(
            cursosDesdeCero = MasterCourseRepository.getCoursesByLanguageAndLevel(language, Nivel.DESDE_CERO),
            cursosPrincipiante = MasterCourseRepository.getCoursesByLanguageAndLevel(language, Nivel.PRINCIPIANTE),
            cursosIntermedio = MasterCourseRepository.getCoursesByLanguageAndLevel(language, Nivel.INTERMEDIO),
            cursosAvanzado = MasterCourseRepository.getCoursesByLanguageAndLevel(language, Nivel.AVANZADO)
        )
    }
}
class MenuViewModelFactory(
    private val userData: UserData?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(userData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}