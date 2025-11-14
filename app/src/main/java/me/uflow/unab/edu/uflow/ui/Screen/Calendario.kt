package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uflow.unab.edu.uflow.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendario(
    onBack: () -> Unit = {}
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title ={ },
                navigationIcon = {
                    IconButton (onClick = onBack){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            // Usa el color del tema
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface (shadowElevation = 8.dp) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically // Centra el botón y el icono
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFAFAFA)),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp)

                    ) {
                        Text(
                            text = "Crear una tarea",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF484848)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Empuja el icono al final

                    IconButton(
                        onClick = { /* TODO: Navegar a la IA */ },
                        modifier = Modifier
                            .background(Color(0xFF0B5696), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Ir a la IA",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Hoy",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B5696)
            )

            // Usa el componente TaskCard
            TaskCard(
                title = "Java Básico: sintáxis y estructuras",
                onClick = {
                    println("Tarjeta Java básico fue presionada")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(
                text = "Mañana",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B5696)
            )

            TaskCard(
                title = "Python Básico: sintáxis y estructuras",
                onClick = {
                    println("Tarjeta Java básico fue presionada")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(
                text = "Próximas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B5696)
            )

            TaskCard(
                title = "Kotlin Básico: sintáxis y estructuras",
                onClick = {
                    println("Tarjeta Java básico fue presionada")
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
    }
}