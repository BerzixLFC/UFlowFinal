package me.uflow.unab.edu.uflow.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class DirectMessage(
    val senderId: String = "",
    val text: String = "",
    
    @ServerTimestamp
    val timestamp: Timestamp? = null
) {
    // Constructor vacío requerido por Firestore
    constructor() : this("", "", null)
}