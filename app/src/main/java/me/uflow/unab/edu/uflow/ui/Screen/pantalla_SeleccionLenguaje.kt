package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.util.Lenguaje

@Composable
fun PantallaSeleccionLenguaje(
    navController: NavController
) {

    var selectedLanguage by remember { mutableStateOf<Lenguaje?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .systemBarsPadding(),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF5660A8))
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¿Por dónde quieres empezar\ntu viaje en programación?",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaPython(
                isSelected = selectedLanguage == Lenguaje.PYTHON,
                onClick = {
                    selectedLanguage =
                        if (selectedLanguage == Lenguaje.PYTHON) null else Lenguaje.PYTHON
                }
            )
            TarjetaJava(
                isSelected = selectedLanguage == Lenguaje.JAVA,
                onClick = {
                    selectedLanguage =
                        if (selectedLanguage == Lenguaje.JAVA) null else Lenguaje.JAVA
                }
            )
            TarjetaKotlin(
                isSelected = selectedLanguage == Lenguaje.KOTLIN,
                onClick = {
                    selectedLanguage =
                        if (selectedLanguage == Lenguaje.KOTLIN) null else Lenguaje.KOTLIN
                }
            )
            TarjetaDesarrolloWeb(
                isSelected = selectedLanguage == Lenguaje.WEB,
                onClick = {
                    selectedLanguage = if (selectedLanguage == Lenguaje.WEB) null else Lenguaje.WEB
                }
            )
        }

        AnimatedVisibility(
            visible = selectedLanguage != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            selectedLanguage?.let { lang ->
                Button(
                    onClick = {
                        // Navega a la siguiente pantalla de onboarding
                        navController.navigate("seleccion_dificultad/${lang.name}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lang.color
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Empezar con ${lang.nombre}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
private fun TarjetaPython(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val PythonBarColor = Lenguaje.PYTHON.color
    val FocusTextColor = Color(0xFF666666)

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(300),
        label = "borderWidth"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) PythonBarColor else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .offset(x = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    bottomStart = 16.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            bottomStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(
                        color = PythonBarColor,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 7.dp, end = 16.dp + 16.dp, bottom = 7.dp)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Python",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.python_icon_menu),
                        contentDescription = "Logo Python",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Ideal para principiantes... y para cambiar el mundo. ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF555555),
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Aprende el lenguaje más versátil del ecosistema tecnológico: simple de leer, rápido de escribir y poderoso en acción.") }
                    },
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Justify,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF000000),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("\uD83D\uDCA1 Enfocado en: ") }
                            withStyle(
                                style = SpanStyle(
                                    color = FocusTextColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("Inteligencia artificial, ciencia de datos, automatización, backend y scripting.") }
                        },
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaJava(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val JavaBarColor = Lenguaje.JAVA.color
    val FocusTextColor = Color(0xFF666666)

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(300),
        label = "borderWidth"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) JavaBarColor else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .offset(x = -16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 16.dp + 16.dp,
                        top = 7.dp,
                        end = 16.dp,
                        bottom = 7.dp
                    )
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Java",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.javabase_logo),
                        contentDescription = "Logo Java",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("La columna vertebral del software empresarial. ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF555555),
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Domina un lenguaje maduro, robusto y diseñado para durar. Esencial en sistemas bancarios y servidores.") }
                    }, lineHeight = 18.sp, textAlign = TextAlign.Justify
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF000000),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("\uD83D\uDCA1 Enfocado en: ") }
                            withStyle(
                                style = SpanStyle(
                                    color = FocusTextColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("Backend empresarial, arquitectura de software y desarrollo Android (clásico).") }
                        }, lineHeight = 18.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(
                        color = JavaBarColor,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            )
        }
    }
}

@Composable
private fun TarjetaKotlin(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val KotlinBarColor = Lenguaje.KOTLIN.color
    val FocusTextColor = Color(0xFF666666)

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(300),
        label = "borderWidth"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) KotlinBarColor else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .offset(x = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    bottomStart = 16.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            bottomStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(
                        color = KotlinBarColor,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 7.dp,
                        end = 16.dp + 16.dp,
                        bottom = 7.dp
                    )
                    .fillMaxHeight()
                    .weight(1f), verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Kotlin",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.kotlin_icon_menu),
                        contentDescription = "Logo Kotlin",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("El futuro del desarrollo Android. ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF555555),
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Moderno, seguro y conciso, Kotlin es el lenguaje oficial para Android y una joya para desarrollo multiplataforma.") }
                    }, lineHeight = 18.sp, textAlign = TextAlign.Justify
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF000000),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("\uD83D\uDCA1 Enfocado en: ") }
                            withStyle(
                                style = SpanStyle(
                                    color = FocusTextColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("Desarrollo Android moderno, Kotlin Multiplatform y código limpio.") }
                        }, lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaDesarrolloWeb(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val WebBarColor = Lenguaje.WEB.color
    val FocusTextColor = Color(0xFF666666)

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(300),
        label = "borderWidth"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) WebBarColor else Color.Transparent,
        animationSpec = tween(300),
        label = "borderColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .offset(x = -16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp + 16.dp, top = 7.dp, end = 16.dp, bottom = 7.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Desarrollo Web",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.desarrolloweblargo_logos),
                        contentDescription = "Logos de HTML, CSS, JS",
                        modifier = Modifier
                            .height(30.dp)
                            .widthIn(min = 20.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Construye lo que ves en tu navegador y más. ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF555555),
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        ) { append("Domina HTML, CSS y JavaScript para crear experiencias de usuario interactivas con frameworks modernos.") }
                    }, lineHeight = 18.sp, textAlign = TextAlign.Justify
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF000000),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("\uD83D\uDCA1 Enfocado en: ") }
                            withStyle(
                                style = SpanStyle(
                                    color = FocusTextColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            ) { append("Frontend, diseño web responsivo, frameworks (React/Vue) y backend (Node).") }
                        }, lineHeight = 18.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(
                        color = WebBarColor,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            )
        }
    }
}