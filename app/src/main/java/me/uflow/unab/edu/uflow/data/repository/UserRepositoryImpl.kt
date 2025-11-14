//ES

package me.uflow.unab.edu.uflow.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.persistentCacheSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
) : UserRepository {
    private val db: FirebaseFirestore
    private val usersCollection: CollectionReference

    init {
        val firestore = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder(firestore.firestoreSettings)
            .setLocalCacheSettings(persistentCacheSettings {})
            .build()
        firestore.firestoreSettings = settings

        this.db = firestore
        this.usersCollection = db.collection("users")
    }
    override val userDataStream: Flow<UserData?> = callbackFlow {
        var firestoreListener: com.google.firebase.firestore.ListenerRegistration? = null
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                firestoreListener?.remove()
                // Creamos el nuevo listener para el documento del usuario
                firestoreListener = usersCollection.document(uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w("UserRepository", "Error al escuchar UserData", e)
                            trySend(null) 
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val userData = snapshot.toObject(UserData::class.java)
                            trySend(userData)
                        } else {
                            trySend(null)
                        }
                    }
            } else {
                // No hay usuario logueado (logout)
                firestoreListener?.remove()
                trySend(null)
            }
        }
        // Adjuntamos el listener de Auth
        auth.addAuthStateListener(authListener)

        // Definimos la limpieza
        awaitClose {
            auth.removeAuthStateListener(authListener)
            firestoreListener?.remove()
        }
    }
    
    //FUNCION DE CREAR SI NO EXISTE, PARA EL REGISTRO. EVITA SOSBRESCRIBIR DATOS DE USUARIO EXISTENTE
    override suspend fun createNewUserData(
        uid: String,
        email: String?,
        nombre: String,
        apellido: String,
        usuario: String
    ) {
        try {
            val document = usersCollection.document(uid)
            val snapshot = document.get().await()

            if (!snapshot.exists()) {
                val newUser = UserData(
                    uid = uid,
                    email = email,
                    nombre = nombre,
                    apellido = apellido,
                    usuario = usuario
                )
                document.set(newUser).await()
                Log.d("UserRepository", "Nuevo UserData creado para $uid")
            } else {
                Log.d("UserRepository", "UserData ya existía para $uid")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al crear UserData", e)
            throw e
        }
    }

    //Funciones de Actualización de Perfil

    override suspend fun saveUserPreferences(uid: String, lenguaje: Lenguaje, nivel: Nivel) {
        try {
            usersCollection.document(uid).update(
                mapOf(
                    "selectedLanguage" to lenguaje,
                    "selectedNivel" to nivel
                )
            ).await()
            Log.d("UserRepository", "Preferencias guardadas para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al guardar preferencias", e)
            throw e
        }
    }

    override suspend fun searchUsersByUsername(query: String, currentUid: String): List<UserData> {
        try {
            val querySnapshot = usersCollection
                .whereGreaterThanOrEqualTo("usuario", query)
                .whereLessThanOrEqualTo("usuario", query + "\uf8ff")
                .limit(10) // Limita a 10 resultados
                .get()
                .await()

            // Mapea los documentos a objetos UserData
            // y filtra al usuario actual (para no buscarse a sí mismo)
            return querySnapshot.documents.mapNotNull {
                it.toObject(UserData::class.java)
            }.filter { it.uid != currentUid }

        } catch (e: Exception) {
            Log.e("UserRepository", "Error al buscar usuarios", e)
            throw e
        }
    }
    
    override suspend fun getUsersFromUids(uids: List<String>): List<UserData> {
        if (uids.isEmpty()) {
            return emptyList()
        }
        try {
            val querySnapshot = usersCollection
                .whereIn("uid", uids)
                .get()
                .await()

            return querySnapshot.documents.mapNotNull {
                it.toObject(UserData::class.java)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al obtener usuarios por UIDs", e)
            throw e
        }
    }

    override suspend fun getAllUsers(currentUid: String): List<UserData> {
        try {
            val querySnapshot = usersCollection
                .limit(20) // Limita a 20 para sugerencias
                .get()
                .await()
            return querySnapshot.documents.mapNotNull {
                it.toObject(UserData::class.java)
            }.filter { it.uid != currentUid }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al obtener todos los usuarios", e)
            throw e
        }
    }
    
    // SIRVE PARA AÑADIR AÑADIR SOLICITUDES DE AMISTAD
    override suspend fun sendFriendRequest(senderUid: String, receiverUid: String) {
        try {
            usersCollection.document(senderUid).update(
                "friendRequestsSent", FieldValue.arrayUnion(receiverUid)
            ).await()
            usersCollection.document(receiverUid).update(
                "friendRequestsReceived", FieldValue.arrayUnion(senderUid)
            ).await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al enviar solicitud", e)
            throw e
        }
    }

    override suspend fun respondToFriendRequest(
        currentUid: String,
        senderUid: String,
        accept: Boolean
    ) {
        try {
            // 1. Quita 'senderUid' de 'friendRequestsReceived' del usuario actual
            usersCollection.document(currentUid).update(
                "friendRequestsReceived", FieldValue.arrayRemove(senderUid)
            ).await()
            // 2. Quita 'currentUid' de 'friendRequestsSent' del otro usuario
            usersCollection.document(senderUid).update(
                "friendRequestsSent", FieldValue.arrayRemove(currentUid)
            ).await()

            if (accept) {
                // 3. (Si acepta) Añade a la lista de 'friends' de ambos
                usersCollection.document(currentUid).update(
                    "friends", FieldValue.arrayUnion(senderUid)
                ).await()
                usersCollection.document(senderUid).update(
                    "friends", FieldValue.arrayUnion(currentUid)
                ).await()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al responder solicitud", e)
            throw e
        }
    }

    override suspend fun removeFriend(currentUid: String, friendUid: String) {
        try {
            // Usa 'arrayRemove' para quitarse de la lista de amigos de ambos
            usersCollection.document(currentUid).update(
                "friends", FieldValue.arrayRemove(friendUid)
            ).await()
            usersCollection.document(friendUid).update(
                "friends", FieldValue.arrayRemove(currentUid)
            ).await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al eliminar amigo", e)
            throw e
        }
    }

    override suspend fun cancelFriendRequest(senderUid: String, receiverUid: String) {
        try {
            // Lo mismo que 'sendFriendRequest' pero con 'arrayRemove'
            usersCollection.document(senderUid).update(
                "friendRequestsSent", FieldValue.arrayRemove(receiverUid)
            ).await()
            usersCollection.document(receiverUid).update(
                "friendRequestsReceived", FieldValue.arrayRemove(senderUid)
            ).await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al cancelar solicitud", e)
            throw e
        }
    }
    
    // FIRESTONE BUSCA LOS CURSOS Y ACTUALIZA O CREA
    override suspend fun updateSessionStatus(uid: String, sessionId: String, status: String) {
        try {
            val fieldPath = "courseProgress.$sessionId" // ej: "courseProgress.kotlin_cero_s1"
            usersCollection.document(uid).update(fieldPath, status).await()
            Log.d("UserRepository", "Progreso de sesión actualizado para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al guardar progreso", e)
            throw e
        }
    }

    override suspend fun addCompletedCourse(uid: String, courseId: String) {
        try {
            // Usa 'arrayUnion' para añadir de forma segura a la lista de completados
            usersCollection.document(uid).update(
                "completedCourses", FieldValue.arrayUnion(courseId)
            ).await()
            Log.d("UserRepository", "Curso completado añadido para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al guardar curso completado", e)
            throw e
        }
    }

    // Funciones de Perfil Extendido

    override suspend fun updateUserConnections(
        uid: String,
        instagram: String,
        twitter: String,
        github: String
    ) {
        try {
            val connectionsData = mapOf(
                "instagramHandle" to instagram,
                "twitterHandle" to twitter,
                "githubHandle" to github
            )
            usersCollection.document(uid).update(connectionsData).await()
            Log.d("UserRepository", "Conexiones actualizadas para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al actualizar conexiones", e)
            throw e
        }
    }

    override suspend fun updateUserProfileInfo(
        uid: String,
        nombre: String,
        apellido: String,
        usuario: String,
        cumpleanos: String,
        localizacion: String
    ) {
        try {
            val infoData = mapOf(
                "nombre" to nombre,
                "apellido" to apellido,
                "usuario" to usuario,
                "cumpleanos" to cumpleanos,
                "localizacion" to localizacion
            )
            usersCollection.document(uid).update(infoData).await()
            Log.d("UserRepository", "Información de perfil actualizada para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al actualizar información", e)
            throw e
        }
    }

    override suspend fun updateUserInterests(uid: String, intereses: List<String>) {
        try {
            // Aquí se actualiza la lista completa (sobrescribe)
            usersCollection.document(uid).update("intereses", intereses).await()
            Log.d("UserRepository", "Intereses actualizados para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al actualizar intereses", e)
            throw e
        }
    }
    
    //FUNCION PARA GUARDAR ESTADISTICAS DE RETO
    override suspend fun updateChallengeStats(
        uid: String,
        newBestStreak: Int,
        newBestStreakLanguage: Lenguaje?,
        newBestStreakDifficulty: Nivel?,
        newAttemptsCount: Int,
        newLastStreak: Int,
        newLastStreakLanguage: Lenguaje?,
        newLastStreakDifficulty: Nivel?
    ) {
        try {
            val statsData = mapOf(
                "bestStreak" to newBestStreak,
                "bestStreakLanguage" to newBestStreakLanguage,
                "bestStreakDifficulty" to newBestStreakDifficulty,
                "attemptsCount" to newAttemptsCount,
                "lastStreak" to newLastStreak,
                "lastStreakLanguage" to newLastStreakLanguage,
                "lastStreakDifficulty" to newLastStreakDifficulty
            )
            // Actualiza el documento
            usersCollection.document(uid).update(statsData).await()
            Log.d("UserRepository", "Estadísticas de reto actualizadas para $uid")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al actualizar estadísticas de reto", e)
            throw e
        }
    } 
}