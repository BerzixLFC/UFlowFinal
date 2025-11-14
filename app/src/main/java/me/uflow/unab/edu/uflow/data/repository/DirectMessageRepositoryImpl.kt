package me.uflow.unab.edu.uflow.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import me.uflow.unab.edu.uflow.data.model.DirectMessage

class DirectMessageRepositoryImpl : DirectMessageRepository {

    private val db: FirebaseFirestore = Firebase.firestore

    // Creamos una nueva colección de nivel superior para las conversaciones
    private val conversationsCollection = db.collection("conversations")
    
    //GENERA UNA ID DE SALA DE CHAT UNICO ORDENANDO LOS UID PARA QUE LO USUARIOS SE ENCUENTREN EN LA MISMA SALA
    override fun getChatRoomId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) {
            "${uid1}_${uid2}"
        } else {
            "${uid2}_${uid1}"
        }
    }

    //ENVIA MENSAJE
    override suspend fun sendMessage(chatRoomId: String, senderId: String, text: String) {
        try {
            // Creamos el mensaje 
            val message = DirectMessage(
                senderId = senderId,
                text = text
            )

            // Añadimos el mensaje a la sub-colección "messages"
            conversationsCollection.document(chatRoomId)
                .collection("messages")
                .add(message)
                .await()

        } catch (e: Exception) {
            Log.e("DMChatRepo", "Error al enviar mensaje", e)
            throw e
        }
    }

    //ESCUCHA EN TIEMPO REAL LO MENSAJES DE LA SALA
    override fun getChatMessages(chatRoomId: String): Flow<List<DirectMessage>> = callbackFlow {
        val query = conversationsCollection.document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Ordena por fecha

        // addSnapshotListener se activa CADA VEZ que hay un cambio
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("DMChatRepo", "Error escuchando mensajes", error)
                close(error) 
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messages = snapshot.documents.mapNotNull {
                    it.toObject(DirectMessage::class.java)
                }
                trySend(messages)
            }
        }
        awaitClose { listener.remove() }
    }
}