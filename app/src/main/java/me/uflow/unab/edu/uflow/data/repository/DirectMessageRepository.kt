package me.uflow.unab.edu.uflow.data.repository

import kotlinx.coroutines.flow.Flow
import me.uflow.unab.edu.uflow.data.model.DirectMessage

interface DirectMessageRepository {

    //Genera un ID del chat
    fun getChatRoomId(uid1: String, uid2: String): String

    //Obtiene todos los mensajes ordenados por fecha
    fun getChatMessages(chatRoomId: String): Flow<List<DirectMessage>>

    
    //Envía un nuevo mensaje a la sala de chat.

    suspend fun sendMessage(chatRoomId: String, senderId: String, text: String)
}