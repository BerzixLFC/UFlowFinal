package me.uflow.unab.edu.uflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.gemini.GeminiChatMessage
import me.uflow.unab.edu.uflow.data.networking.GeminiApiService
import me.uflow.unab.edu.uflow.data.repository.GeminiChatRepository
data class GeminiChatUiState(
    val messages: List<GeminiChatMessage> = emptyList(),
    val isLoading: Boolean = false
)
class GeminiChatViewModel : ViewModel() {
    private val apiService = GeminiApiService()
    private val repository = GeminiChatRepository(apiService)
    private val _uiState = MutableStateFlow(GeminiChatUiState())
    val uiState: StateFlow<GeminiChatUiState> = _uiState.asStateFlow()
    fun sendMessage(userMessage: String){
        if (userMessage.isBlank() || _uiState.value.isLoading) return
        val userMsg = GeminiChatMessage(text = userMessage, isFromUser = true)
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMsg,
                isLoading = true
            )
        }
        viewModelScope.launch {
            val result = repository.getChatResponse(userMessage)
            result.fold(
                onSuccess = { aiResponseText ->
                    val aiMsg = GeminiChatMessage(text = aiResponseText, isFromUser = false)
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + aiMsg,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    val errorMsg = GeminiChatMessage(text = "Error: ${error.message}", isFromUser = false)
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + errorMsg,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}