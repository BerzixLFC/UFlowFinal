package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.uflow.unab.edu.uflow.data.model.UserData
import me.uflow.unab.edu.uflow.data.repository.UserRepository
import me.uflow.unab.edu.uflow.data.repository.UserRepositoryImpl
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.ui.Screen.PantallaPerfilUI
data class FriendProfileUiState(
    val userData: UserData? = null,
    val isLoading: Boolean = true
)
class FriendProfileViewModel(friendUid: String) : ViewModel() {
    private val userRepository: UserRepository = UserRepositoryImpl(Firebase.auth)

    private val _uiState = MutableStateFlow(FriendProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFriendData(friendUid)
    }

    private fun loadFriendData(friendUid: String) {
        viewModelScope.launch {
            // Carga solo los datos del amigo
            val result = userRepository.getUsersFromUids(listOf(friendUid))
            _uiState.value = FriendProfileUiState(
                userData = result.firstOrNull(),
                isLoading = false
            )
        }
    }
}
class FriendProfileViewModelFactory(private val friendUid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendProfileViewModel(friendUid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun PantallaPerfilAmigo(
    navController: NavController,
    authViewModel: AuthViewModel,
    friendUid: String
) {
    val factory = FriendProfileViewModelFactory(friendUid)
    val viewModel: FriendProfileViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsState()
    val friendData = uiState.userData
    
    val authState by authViewModel.uiState.collectAsState()
    val currentUserData = authState.userData
    
    val relationshipStatus by remember(currentUserData, friendUid) {
        derivedStateOf {
            when {
                currentUserData == null -> "LOADING"
                currentUserData.friends.contains(friendUid) -> "FRIEND"
                currentUserData.friendRequestsSent.contains(friendUid) -> "SENT"
                currentUserData.friendRequestsReceived.contains(friendUid) -> "RECEIVED"
                else -> "NONE"
            }
        }
    }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmRemoveFriendDialog(
            friendName = friendData?.getFullName() ?: "este amigo",
            onConfirm = {
                authViewModel.removeFriend(friendUid) 
                showConfirmDialog = false
                navController.popBackStack()
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }
    PantallaPerfilUI(
        navController = navController,
        userData = friendData,
        isMyProfile = false,
        onLogout = {},
        onChat = {
            if (friendData != null) {
                navController.navigate("chat/${friendData.uid}/${friendData.getFullName()}")
            }
        },
        onRemoveFriend = {
            showConfirmDialog = true
        },
        relationshipStatus = relationshipStatus,
        onAddFriend = {
            authViewModel.sendFriendRequest(friendUid)
        },
        onAcceptFriend = {
            authViewModel.respondToFriendRequest(friendUid, true)
        },
        onRejectFriend = {
            authViewModel.respondToFriendRequest(friendUid, false)
        },
        onNavigateToAllCourses = {},
        onNavigateToEditConnections = {},
        onNavigateToEditInfo = {},
        onNavigateToEditInterests = {},
        onCancelRequest = {
            authViewModel.cancelFriendRequest(friendUid)
        }
    )
}

@Composable
private fun ConfirmRemoveFriendDialog(
    friendName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, contentDescription = "Alerta", tint = Color.Red) },
        title = { Text(text = "Eliminar Amigo") },
        text = { Text(text = "¿Estás seguro de que quieres eliminar a $friendName de tu lista de amigos?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Eliminar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}