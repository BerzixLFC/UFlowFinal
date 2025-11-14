package me.uflow.unab.edu.uflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.ChatMessage

class ChatViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _chatUiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val chatUiState: StateFlow<ChatUiState> = _chatUiState
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages
    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            db.collection("chats").document(chatId).collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        _chatUiState.value = ChatUiState.Error("Error al cargar mensajes")
                        return@addSnapshotListener
                    }
                    val messageList = snapshot?.documents?.mapNotNull {
                        it.toObject(ChatMessage::class.java)
                    } ?: emptyList()
                    _messages.value = messageList
                    _chatUiState.value = ChatUiState.Success
                }
        }
    }

    fun sendMessage(chatId: String, text: String) {
        val userId = auth.currentUser?.uid ?: return
        val message = ChatMessage(
            senderId = userId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            db.collection("chats").document(chatId).collection("messages")
                .add(message)
                .addOnFailureListener {
                }
        }
    }
}