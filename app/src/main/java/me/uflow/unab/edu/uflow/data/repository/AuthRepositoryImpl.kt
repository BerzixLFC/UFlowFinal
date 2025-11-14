//ES

package me.uflow.unab.edu.uflow.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import me.uflow.unab.edu.uflow.data.model.User

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {
    
    override fun getAuthStateStream(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                trySend(null)
            } else {
                trySend(User(uid = firebaseUser.uid, email = firebaseUser.email))
            }
        }
        //Registra el listener
        auth.addAuthStateListener(listener)

        //'awaitClose' se llamará cuando el Flow se cancele
        awaitClose {
            // Limpia (remueve) el listener
            auth.removeAuthStateListener(listener)
        }
    }
    
    override suspend fun loginUserWithEmail(email: String, pass: String): User {
        // La corrutina se "suspende" aquí hasta que Firebase responda
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        // Esta línea solo se ejecuta si .await() tuvo éxito
        val firebaseUser =
            result.user ?: throw Exception("Error de Firebase: Usuario nulo después de login")
        return User(uid = firebaseUser.uid, email = firebaseUser.email)
    }

    override suspend fun createUserWithEmail(email: String, pass: String): User {
        // Se usa la misma técnica .await()
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val firebaseUser =
            result.user ?: throw Exception("Error de Firebase: Usuario nulo después de registro")
        return User(uid = firebaseUser.uid, email = firebaseUser.email)
    }

    override suspend fun signInWithGoogleToken(idToken: String): User {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        // Se usa la misma técnica .await()
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user
            ?: throw Exception("Error de Firebase: Usuario nulo después de login con Google")
        return User(uid = firebaseUser.uid, email = firebaseUser.email)
    }
    
    override suspend fun signInWithGitHub(activity: Activity): User {
        val provider = OAuthProvider.newBuilder("github.com").build()
        return suspendCoroutine<User> { continuation ->
            auth.startActivityForSignInWithProvider(activity, provider)
                .addOnSuccessListener { result ->
                    val firebaseUser = result.user
                        ?: throw Exception("Error de Firebase: Usuario nulo después de login con GitHub")
                    continuation.resumeWith(
                        Result.success(
                            User(
                                uid = firebaseUser.uid,
                                email = firebaseUser.email
                            )
                        )
                    )
                }
                .addOnFailureListener { e ->
                    continuation.resumeWith(Result.failure(e))
                }
        }
    }
    override suspend fun logout() {
        auth.signOut()
    }
}