package me.uflow.unab.edu.uflow.ui.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.data.model.Session
import me.uflow.unab.edu.uflow.data.model.SessionStatus
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.ui.viewmodel.CursoViewModel
import me.uflow.unab.edu.uflow.ui.viewmodel.CursoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleCurso(
    navController: NavController,
    courseId: String,
    lenguajeName: String,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val userData = authState.userData
    val progressMap = userData?.courseProgress ?: emptyMap()
    val completedCourses = userData?.courses ?: emptyList()

    val factory = CursoViewModelFactory(courseId, lenguajeName, progressMap)
    val viewModel: CursoViewModel = viewModel(
        key = progressMap.toString(),
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    val languageColor = viewModel.language.color
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            languageColor.copy(alpha = 0.8f),
            languageColor.copy(alpha = 0.6f)
        )
    )

    var expandedSessionId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        if (uiState.progressFloat >= 1.0f) {
            if (!completedCourses.contains(courseId)) {
                authViewModel.addCompletedCourse(courseId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFFF0F2F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Text(
                    text = uiState.courseTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiState.courseSubtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            //Cuerpo
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), 
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp 
                )
            ) {
                // 1. La barra de progreso AHORA ES UN ITEM de la lista
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Progreso: ${uiState.progressText}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { uiState.progressFloat },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = languageColor,
                                trackColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                //Mostramos las lecciones
                if (uiState.sessionsWithStatus.isEmpty()) {
                    item {
                        Text(
                            "¡Este curso está en construcción! Vuelve pronto.",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
                items(uiState.sessionsWithStatus) { (session, status) ->
                    SessionListItem(
                        session = session,
                        status = status,
                        isExpanded = expandedSessionId == session.id,
                        onClick = {
                            expandedSessionId = if (expandedSessionId == session.id) null else session.id
                        },
                        onStartClick = {
                            if (status != SessionStatus.COMPLETED) {
                                authViewModel.updateSessionStatus(session.id, SessionStatus.IN_PROGRESS.name)
                            }
                            navController.navigate("leccion_activa/$courseId/${session.id}/$lenguajeName")
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun SessionListItem(
    session: Session,
    status: SessionStatus,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onStartClick: () -> Unit
) {
    val isEnabled = status != SessionStatus.LOCKED

    val (icon, iconColor) = when (status) {
        SessionStatus.COMPLETED -> Icons.Default.CheckCircle to Color(0xFF4CAF50)
        SessionStatus.IN_PROGRESS -> Icons.Default.HourglassEmpty to MaterialTheme.colorScheme.primary
        SessionStatus.LOCKED -> Icons.Default.Block to Color.Red.copy(alpha = 0.7f)
    }

    val titleColor = if (isEnabled) Color.Black else Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = session.title,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = status.name,
                    tint = iconColor
                )
            }

            AnimatedVisibility(visible = isExpanded && isEnabled) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = session.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = iconColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        val buttonText = if (status == SessionStatus.COMPLETED) {
                            "Volverlo a intentar"
                        } else {
                            "Empezar"
                        }
                        Text(buttonText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}