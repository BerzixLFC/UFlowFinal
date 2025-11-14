package me.uflow.unab.edu.uflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.DirectMessage
import me.uflow.unab.edu.uflow.data.repository.DirectMessageRepository
import me.uflow.unab.edu.uflow.data.repository.DirectMessageRepositoryImpl
data class DirectMessageUiState(
    val messages: List<DirectMessage> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
class DirectMessageViewModel(
    val currentUserUid: String,
    private val friendUid: String
) : ViewModel() {
    private val repository: DirectMessageRepository = DirectMessageRepositoryImpl()
    private val _uiState = MutableStateFlow(DirectMessageUiState())
    val uiState = _uiState.asStateFlow()

    private val chatRoomId: String

    init {
        chatRoomId = repository.getChatRoomId(currentUserUid, friendUid)
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            repository.getChatMessages(chatRoomId).collect { messages ->
                _uiState.value = DirectMessageUiState(
                    messages = messages,
                    isLoading = false
                )
            }
        }
    }
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                repository.sendMessage(chatRoomId, currentUserUid, text)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error al enviar mensaje")
            }
        }
    }
}
class DirectMessageViewModelFactory(
    private val currentUserUid: String,
    private val friendUid: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectMessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirectMessageViewModel(currentUserUid, friendUid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}