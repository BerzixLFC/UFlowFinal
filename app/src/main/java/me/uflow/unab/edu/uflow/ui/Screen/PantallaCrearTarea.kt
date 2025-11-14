package me.uflow.unab.edu.uflow.ui.Screen

// En app/src/main/java/me/uflow/unab/edu/uflow/ui/Screen/PantallaCrearTarea.kt


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    onBack: () -> Unit,
    onSaveTask: (String, String, String) -> Unit,
    initialDate: LocalDate // Capturar el día para el que se crea la tarea
) {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Tarea para el ${initialDate.dayOfMonth}/${initialDate.monthValue}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalles de la Tarea", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Materia") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Detalles (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Aquí llamaremos a la función que guarda en Firebase
                    if (title.isNotBlank() && subject.isNotBlank()) {
                        onSaveTask(title, details, subject)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Guardar Tarea", fontSize = 18.sp)
            }
        }
    }
}