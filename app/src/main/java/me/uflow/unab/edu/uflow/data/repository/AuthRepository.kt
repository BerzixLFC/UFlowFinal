package me.uflow.unab.edu.uflow.data.repository

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import me.uflow.unab.edu.uflow.data.model.User
interface AuthRepository {
    fun getAuthStateStream(): Flow<User?>
    suspend fun loginUserWithEmail(email: String, pass: String): User
    suspend fun createUserWithEmail(email: String, pass: String): User
    suspend fun signInWithGoogleToken(idToken: String): User
    suspend fun signInWithGitHub(activity: Activity): User
    suspend fun logout()
}