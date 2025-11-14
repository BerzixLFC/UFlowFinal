package me.uflow.unab.edu.uflow.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Task(
    // Esta anotación le dice a Firestore que aquí debe poner el ID del documento
    @DocumentId val id: String = "",

    // Los nombres de estas propiedades DEBEN coincidir con los campos en tu base de datos Firestore
    val userId: String = "",
    val title: String = "",
    val details: String = "",
    val subject: String = "",
    val date: Timestamp = Timestamp.now(),
    val isCompleted: Boolean = false,
    val createdAt: Timestamp = Timestamp.now()
)
