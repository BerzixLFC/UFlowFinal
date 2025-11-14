package me.uflow.unab.edu.uflow.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.data.repository.AuthRepository
import me.uflow.unab.edu.uflow.data.repository.AuthRepositoryImpl
import me.uflow.unab.edu.uflow.data.repository.UserRepository
import me.uflow.unab.edu.uflow.data.repository.UserRepositoryImpl
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

data class AuthUiState(
    val isInitializing: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: UserData? = null
)

data class AmigosUiState(
    val searchResults: List<UserData> = emptyList(),
    val friendRequests: List<UserData> = emptyList(),
    val friendsList: List<UserData> = emptyList(),
    val suggestionsList: List<UserData> = emptyList(),
    val isLoadingSearch: Boolean = false,
    val isLoadingRequests: Boolean = false,
    val isLoadingFriends: Boolean = false,
    val isLoadingSuggestions: Boolean = false
)


class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _amigosState = MutableStateFlow(AmigosUiState())
    val amigosState: StateFlow<AmigosUiState> = _amigosState.asStateFlow()

    init {
        val firebaseAuth = Firebase.auth
        authRepository = AuthRepositoryImpl(firebaseAuth)
        userRepository = UserRepositoryImpl(firebaseAuth)
        val authUserStream = authRepository.getAuthStateStream()
        val userDataStream = userRepository.userDataStream

        viewModelScope.launch {
            combine(authUserStream, userDataStream) { authUser, userData ->
                if (authUser == null) {
                    AuthUiState(isInitializing = false, userData = null)
                } else {
                    if (userData == null) {
                        // Pasamos un UserData temporal si no existe en Firestore
                        AuthUiState(isInitializing = false, userData = UserData(uid = authUser.uid, email = authUser.email))
                    } else {
                        AuthUiState(isInitializing = false, userData = userData)
                    }
                }
            }.collect { newState ->
                _uiState.update { newState }
            }
        }

        viewModelScope.launch {
            _uiState.collect { authState ->
                val userData = authState.userData
                if (userData != null && !authState.isInitializing) { // Asegúrate que no sea el estado inicial
                    loadFriendProfiles(userData.friends)
                    loadFriendRequestProfiles(userData.friendRequestsReceived)
                    loadAllUserSuggestions()
                } else {
                    _amigosState.update { AmigosUiState() } // Limpiar si el usuario cierra sesión
                }
            }
        }
    }


    fun loginUserWithEmail(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val loggedInUser = authRepository.loginUserWithEmail(email, pass)
                // createNewUserData revisará si el usuario ya existe, así que es seguro llamarlo
                val tempUsuario = loggedInUser.email?.split("@")?.get(0) ?: "usuario"
                userRepository.createNewUserData(loggedInUser.uid, loggedInUser.email, "", "", tempUsuario)
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrecta"
                    is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                    else -> "Error al iniciar sesión: ${e.message}"
                }
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun createUserWithEmail(email: String, pass: String, nombre: String, apellido: String, usuario: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val newUser = authRepository.createUserWithEmail(email, pass)
                userRepository.createNewUserData(newUser.uid, newUser.email, nombre, apellido, usuario)
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo"
                    else -> "Error al registrar: ${e.message}"
                }
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val googleUser = authRepository.signInWithGoogleToken(idToken)
                val nombre = googleUser.email?.split("@")?.get(0) ?: "Usuario"
                val usuario = googleUser.email?.split("@")?.get(0) ?: "usuario"
                userRepository.createNewUserData(googleUser.uid, googleUser.email, nombre, "Google", usuario)

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error con Google: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun signInWithGitHub(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val githubUser = authRepository.signInWithGitHub(activity)
                val nombre = githubUser.email?.split("@")?.get(0) ?: "Usuario"
                val usuario = githubUser.email?.split("@")?.get(0) ?: "usuario"
                userRepository.createNewUserData(githubUser.uid, githubUser.email, nombre, "GitHub", usuario)

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error con GitHub: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setUserPreferences(lenguaje: Lenguaje, nivel: Nivel) {
        val uid = _uiState.value.userData?.uid
        if (uid == null) {
            _uiState.update { it.copy(error = "Error: No se pudo guardar (Usuario no logueado)") }
            return
        }
        viewModelScope.launch {
            try {
                userRepository.saveUserPreferences(uid, lenguaje, nivel)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al guardar preferencias", e)
                _uiState.update { it.copy(error = "Error al guardar preferencias") }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.update { AuthUiState(isInitializing = false) } // Limpiar estado al salir
            _amigosState.update { AmigosUiState() } // Limpiar estado de amigos
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }


    // --- FUNCIONES DE AMIGOS (SIN TOCAR) ---

    private fun getCurrentUid(): String? {
        return _uiState.value.userData?.uid
    }

    fun searchUsersByUsername(query: String) {
        val uid = getCurrentUid() ?: return

        viewModelScope.launch {
            _amigosState.update { it.copy(isLoadingSearch = true) }
            try {
                val results = userRepository.searchUsersByUsername(query, uid)
                _amigosState.update { it.copy(searchResults = results) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al buscar") }
            } finally {
                _amigosState.update { it.copy(isLoadingSearch = false) }
            }
        }
    }

    fun sendFriendRequest(receiverUid: String) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.sendFriendRequest(uid, receiverUid)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al enviar solicitud") }
            }
        }
    }

    fun respondToFriendRequest(senderUid: String, accept: Boolean) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.respondToFriendRequest(uid, senderUid, accept)
            } catch (e: Exception)
            {
                _uiState.update { it.copy(error = "Error al responder solicitud") }
            }
        }
    }

    fun removeFriend(friendUid: String) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.removeFriend(uid, friendUid)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al eliminar amigo") }
            }
        }
    }

    fun cancelFriendRequest(receiverUid: String) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.cancelFriendRequest(uid, receiverUid)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cancelar solicitud") }
            }
        }
    }

    private fun loadFriendProfiles(uids: List<String>) {
        viewModelScope.launch {
            _amigosState.update { it.copy(isLoadingFriends = true) }
            try {
                val profiles = userRepository.getUsersFromUids(uids)
                _amigosState.update { it.copy(friendsList = profiles) }
            } finally {
                _amigosState.update { it.copy(isLoadingFriends = false) }
            }
        }
    }

    private fun loadFriendRequestProfiles(uids: List<String>) {
        viewModelScope.launch {
            _amigosState.update { it.copy(isLoadingRequests = true) }
            try {
                val profiles = userRepository.getUsersFromUids(uids)
                _amigosState.update { it.copy(friendRequests = profiles) }
            } finally {
                _amigosState.update { it.copy(isLoadingRequests = false) }
            }
        }
    }

    fun loadAllUserSuggestions() {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            _amigosState.update { it.copy(isLoadingSuggestions = true) }
            try {
                val users = userRepository.getAllUsers(uid)
                _amigosState.update { it.copy(suggestionsList = users) }
            } finally {
                _amigosState.update { it.copy(isLoadingSuggestions = false) }
            }
        }
    }

    // --- FIN FUNCIONES DE AMIGOS ---

    fun updateSessionStatus(sessionId: String, status: String) {
        val uid = getCurrentUid() ?: return

        viewModelScope.launch {
            try {
                userRepository.updateSessionStatus(uid, sessionId, status)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar progreso") }
            }
        }
    }

    fun addCompletedCourse(courseId: String) {
        val uid = getCurrentUid() ?: return

        viewModelScope.launch {
            try {
                userRepository.addCompletedCourse(uid, courseId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar curso completado") }
            }
        }
    }

    fun cycleUserNivel() {
        val uid = getCurrentUid() ?: return
        val currentUserData = _uiState.value.userData ?: return
        val currentNivel = currentUserData.selectedNivel ?: Nivel.DESDE_CERO
        val currentLang = currentUserData.selectedLanguage ?: Lenguaje.KOTLIN

        val allNivels = Nivel.entries
        val currentIndex = allNivels.indexOf(currentNivel)
        val nextIndex = (currentIndex + 1) % allNivels.size
        val newNivel = allNivels[nextIndex]

        viewModelScope.launch {
            try {
                userRepository.saveUserPreferences(uid, currentLang, newNivel)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cambiar nivel") }
            }
        }
    }

    fun updateUserConnections(instagram: String, twitter: String, github: String) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.updateUserConnections(uid, instagram, twitter, github)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar conexiones") }
            }
        }
    }

    fun updateUserProfileInfo(nombre: String, apellido: String, usuario: String, cumpleanos: String, localizacion: String) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.updateUserProfileInfo(uid, nombre, apellido, usuario, cumpleanos, localizacion)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar información") }
            }
        }
    }

    fun updateUserInterests(intereses: List<String>) {
        val uid = getCurrentUid() ?: return
        viewModelScope.launch {
            try {
                userRepository.updateUserInterests(uid, intereses)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar intereses") }
            }
        }
    }
    
    suspend fun getFriendsBestStreak(): Pair<Int, String>? {
        val friendsUids = _uiState.value.userData?.friends ?: emptyList()
        if (friendsUids.isEmpty()) {
            return null
        }

        return try {
            val friendsData = userRepository.getUsersFromUids(friendsUids)
            val bestFriend = friendsData.maxByOrNull { it.bestStreak }

            if (bestFriend != null && bestFriend.bestStreak > 0) {
                Pair(bestFriend.bestStreak, bestFriend.usuario)
            } else {
                null // Ningún amigo tiene una racha > 0
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error al obtener la mejor racha de amigos: ${e.message}")
            null
        }
    }
}