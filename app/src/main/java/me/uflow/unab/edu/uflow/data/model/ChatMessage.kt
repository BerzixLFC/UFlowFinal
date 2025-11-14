package me.uflow.unab.edu.uflow.data.model

//ESTE MODELO ES EL QUE SE USA EN LA BASE DE DATOS,
// PARA CONVERSAR CON OTROS USUARIOS, NO EL DE GEMINI

data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val isRead: Boolean = false
)