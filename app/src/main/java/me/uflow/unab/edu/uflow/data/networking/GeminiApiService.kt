package me.uflow.unab.edu.uflow.data.networking

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.uflow.unab.edu.uflow.data.model.gemini.Content
import me.uflow.unab.edu.uflow.data.model.gemini.ErrorResponse
import me.uflow.unab.edu.uflow.data.model.gemini.GeminiRequest
import me.uflow.unab.edu.uflow.data.model.gemini.GeminiResponse
import me.uflow.unab.edu.uflow.data.model.gemini.Part

class GeminiApiService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=${GeminiApiKeyProvider.API_KEY}"

    suspend fun generateContent(prompt: String): Result<String> {
        return try {
            val requestBody = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt)
                        )
                    )
                )
            )

            //Obtenemos la respuesta HTTP completa
            val response: HttpResponse = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            //Verificamos si la respuesta
            if (response.status.isSuccess()) {
                // Si fue exitosa, la leemos como GeminiResponse
                val geminiResponse: GeminiResponse = response.body()

                val responseText = geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (responseText != null) {
                    Result.success(responseText)
                } else {
                    Log.e("GeminiApiService", "Respuesta exitosa pero sin contenido.")
                    Result.failure(Exception("Respuesta exitosa pero vacía."))
                }
            } else {
                //Si NO fue exitosa, la leemos como ErrorResponse
                val errorResponse: ErrorResponse = response.body()
                val errorMessage = errorResponse.error.message
                Log.e("GeminiApiService", "Error de API (HTTP ${response.status}): $errorMessage")
                Result.failure(Exception("Error de API: $errorMessage"))
            }

        } catch (e: Exception) {
            //Captura errores 
            Log.e("GeminiApiService", "Error en la llamada: ${e.message}", e)
            Result.failure(e)
        }
    }
}