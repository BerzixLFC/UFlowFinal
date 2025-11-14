package me.uflow.unab.edu.uflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class EventoViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // Función para crear una nueva tarea
    fun addTask(title: String, details: String, subject: String, date: LocalDate) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            // Convertir LocalDate a Timestamp de Firebase
            val timestamp = Timestamp(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

            val task = hashMapOf(
                "userId" to userId,
                "title" to title,
                "details" to details,
                "subject" to subject,
                "date" to timestamp,
                "isCompleted" to false,
                "createdAt" to Timestamp.Companion.now()
            )

            // Añadimos la tarea a la colección "tasks"
            db.collection("tasks").add(task)
                .addOnSuccessListener {
                    // Éxito, podrías mostrar un Toast o un Snackbar
                }
                .addOnFailureListener {
                    // Error, manejarlo adecuadamente
                }
        }
    }

    // Aquí irían las funciones para OBTENER las tareas, editarlas, borrarlas, etc.
}