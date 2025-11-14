package me.uflow.unab.edu.uflow.data.repository

import me.uflow.unab.edu.uflow.data.networking.GeminiApiService
class GeminiChatRepository(private val apiService: GeminiApiService) {

    // Pasa la llamada directamente al servicio de API
    suspend fun getChatResponse(prompt: String): Result<String> {
        return apiService.generateContent(prompt)
    }
}