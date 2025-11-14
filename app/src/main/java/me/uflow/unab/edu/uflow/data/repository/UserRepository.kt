package me.uflow.unab.edu.uflow.data.repository

import kotlinx.coroutines.flow.Flow
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

interface UserRepository {

    val userDataStream: Flow<UserData?>

    suspend fun createNewUserData(uid: String, email: String?, nombre: String, apellido: String, usuario: String)

    suspend fun saveUserPreferences(uid: String, lenguaje: Lenguaje, nivel: Nivel)

    suspend fun searchUsersByUsername(query: String, currentUid: String): List<UserData>

    suspend fun sendFriendRequest(senderUid: String, receiverUid: String)

    suspend fun respondToFriendRequest(receiverUid: String, senderUid: String, accept: Boolean)

    suspend fun removeFriend(currentUid: String, friendUid: String)
    
    suspend fun cancelFriendRequest(senderUid: String, receiverUid: String)

    suspend fun getUsersFromUids(uids: List<String>): List<UserData>

    suspend fun getAllUsers(currentUid: String): List<UserData>

    suspend fun updateSessionStatus(uid: String, sessionId: String, status: String)

    suspend fun addCompletedCourse(uid: String, courseId: String)

    suspend fun updateUserConnections(uid: String, instagram: String, twitter: String, github: String)

    suspend fun updateUserProfileInfo(
        uid: String,
        nombre: String,
        apellido: String,
        usuario: String,
        cumpleanos: String,
        localizacion: String
    )

    suspend fun updateUserInterests(uid: String, intereses: List<String>)

    //FUNCIONES PARA GUARDAR LA STATS DE RETO
    suspend fun updateChallengeStats(
        uid: String,
        newBestStreak: Int,
        newBestStreakLanguage: Lenguaje?,
        newBestStreakDifficulty: Nivel?,
        newAttemptsCount: Int,
        newLastStreak: Int,
        newLastStreakLanguage: Lenguaje?,
        newLastStreakDifficulty: Nivel?
    )
}