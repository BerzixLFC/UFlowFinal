package me.gabrielgalvis.uflowapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importaciones necesarias al principio de tu archivo
@OptIn(ExperimentalMaterial3Api::class) // Necesario para Card
@Composable
fun TaskCard(
    title: String,
    onClick: () -> Unit // La acción que se ejecutará al hacer clic
) {
    Card(
        // El Modifier es la clave para la interactividad y el estilo
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(onClick = onClick), // ¡Aquí se hace cliqueable!

        // Estilo visual de la tarjeta
        shape = RoundedCornerShape(16.dp), // Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Color de fondo
        )
    ) {
        // Contenido interno de la tarjeta, alineado horizontalmente
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Centra los elementos verticalmente
        ) {
            // 1. Icono
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Icono de tarea",
                tint = MaterialTheme.colorScheme.primary, // Usa el color primario del tema
                modifier = Modifier.size(40.dp)
            )

            // Espacio entre el icono y el texto
            Spacer(modifier = Modifier.width(16.dp))

            // 2. Columna para el texto
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}