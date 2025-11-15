package me.uflow.unab.edu.uflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.Task
import java.time.LocalDate
import java.time.ZoneId

class TaskViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // Este StateFlow mantiene la lista de tareas para el día seleccionado.
    // La UI escucha los cambios en esta variable.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList<Task>())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun loadTasksForDate(date: LocalDate) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val startOfDay = Timestamp(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val endOfDay = Timestamp(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

            // Creamos una consulta a Firestore que se actualiza en tiempo real.
            db.collection("tasks")
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("date", startOfDay)
                .whereLessThan("date", endOfDay)
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        // Si hay un error, lo ideal sería registrarlo o mostrar un mensaje.
                        _tasks.value = emptyList() // Limpiamos la lista para no mostrar datos incorrectos.
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        // Si la consulta es exitosa, convierte los documentos a objetos `Task`.
                        // Esta es la forma moderna que SÍ funciona con Timestamps
                        val taskList = snapshots.toObjects(Task::class.java)
                        _tasks.value = taskList
                    }
                }
        }
    }

    /**
     * Añade una nueva tarea a Firestore.
     * @param title El título de la tarea.
     * @param details Los detalles de la tarea.
     * @param subject La materia asociada.
     * @param date El día para el cual se agenda la tarea.
     */
    fun addTask(title: String, details: String, subject: String, date: LocalDate) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            // Convierte la fecha local de la UI a un Timestamp de Firebase.
            val timestamp = Timestamp(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

            // Crea un mapa de datos que coincide con la estructura en Firestore.
            val task = hashMapOf(
                "userId" to userId,
                "title" to title,
                "details" to details,
                "subject" to subject,
                "date" to timestamp,
                "isCompleted" to false,
                "createdAt" to Timestamp.now()
            )

            // Añade el nuevo documento a la colección "tasks".
            db.collection("tasks").add(task)
                .addOnSuccessListener {
                    // Éxito. No necesitamos hacer nada aquí porque el SnapshotListener
                    // se encargará de actualizar la UI automáticamente.
                }
                .addOnFailureListener { e ->
                    // Fallo. En una app real, aquí deberíamos notificar al usuario.
                }
        }
    }
}



