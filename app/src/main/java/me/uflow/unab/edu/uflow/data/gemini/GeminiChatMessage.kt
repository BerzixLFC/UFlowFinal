package me.uflow.unab.edu.uflow.data.model.gemini

// Modelo simple para la UI de Gemin
data class GeminiChatMessage(
    val text: String,
    val isFromUser: Boolean
)