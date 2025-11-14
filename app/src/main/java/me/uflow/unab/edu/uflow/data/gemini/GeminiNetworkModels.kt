package me.uflow.unab.edu.uflow.data.model.gemini

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

//Texto que recibe el usuario
@Serializable
data class Part(
    val text: String
)

//Respuesta de Gemini
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>
)

//CONTENIDO De la Respuesya de Gemini
@Serializable
data class Candidate(
    val content: Content
)


//COMOPONENTE DE ERRORES; PARA VERFICAR LOS PROBLEMAS EN LA API

@Serializable
data class ErrorResponse(
    val error: ErrorDetails
)

@Serializable
data class ErrorDetails(
    val code: Int,       
    val message: String, 
    val status: String   
)