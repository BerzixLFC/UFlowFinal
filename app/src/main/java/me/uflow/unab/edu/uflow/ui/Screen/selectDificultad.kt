package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel
import me.uflow.unab.edu.uflow.data.repository.RoadmapRepository
import me.uflow.unab.edu.uflow.data.repository.NivelInfo

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipTema(emoji: String, nombre: String) {
    val chipColors = listOf(
        Color(0xFFF0F9FF), Color(0xFFF9F0FF), Color(0xFFFFE0E0),
        Color(0xFFFFF7E0), Color(0xFFE8FFE0), Color(0xFFFFE0F0)
    )
    val chipBackgroundColor = chipColors.random()

    Box(
        modifier = Modifier
            .background(chipBackgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFDCDCDC), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "$emoji $nombre",
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF555555)
        )
    }
}

@Composable
private fun NivelHeader(emojiIcon: String, titulo: String, subtitulo: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = emojiIcon, fontSize = 20.sp)
        Column {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = subtitulo,
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF555555)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarjetaNivel(info: NivelInfo, isSelected: Boolean, onClick: () -> Unit) {
    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(300),
        label = "borderWidth"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) info.color else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            NivelHeader(
                emojiIcon = info.emoji,
                titulo = info.titulo,
                subtitulo = info.subtitulo,
                color = info.color
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                info.chips.forEach { (emoji, nombre) -> ChipTema(emoji = emoji, nombre = nombre) }
            }
        }
    }
}


@Composable
fun PantallaSeleccionDificultad(
    lenguaje: Lenguaje,
    navController: NavController
) {
    val viewModel: AuthViewModel = viewModel()

    var selectedNivelInfo by remember { mutableStateOf<NivelInfo?>(null) }

    val roadmap = remember(lenguaje) { RoadmapRepository.getRoadmap(lenguaje) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
            .systemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF333333),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("¿Por dónde quieres empezar\ntu aprendizaje en ") }
                    withStyle(
                        SpanStyle(
                            color = lenguaje.color,
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) { append("${lenguaje.nombre}?") }
                },
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            roadmap.forEach { info ->
                TarjetaNivel(
                    info = info,
                    isSelected = selectedNivelInfo?.nivel == info.nivel,
                    onClick = {
                        selectedNivelInfo =
                            if (selectedNivelInfo?.nivel == info.nivel) null else info
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        AnimatedVisibility(
            visible = selectedNivelInfo != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            selectedNivelInfo?.let { info ->
                Button(
                    onClick = {
                        // --- ¡¡AQUÍ SE GUARDA!! ---
                        viewModel.setUserPreferences(lenguaje, info.nivel)
                        navController.navigate("menu") {
                            popUpTo("seleccion_lenguaje") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = info.color
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Empezar en ${info.titulo}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Preview Python")
@Composable
fun PreviewDificultadPython() {
    PantallaSeleccionDificultad(lenguaje = Lenguaje.PYTHON, navController = rememberNavController())
}