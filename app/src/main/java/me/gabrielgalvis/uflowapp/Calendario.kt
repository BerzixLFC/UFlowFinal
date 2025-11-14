package me.gabrielgalvis.uflowapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.gabrielgalvis.uflowapp.components.TaskCard
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendario(
    onBack: () -> Unit = {}
) {
    // --- ESTADOS DEL CALENDARIO ---
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    Scaffold (
        topBar = {
            TopAppBar(
                title ={ },
                navigationIcon = {
                    IconButton (onClick = onBack){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black
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
                    horizontalArrangement = Arrangement.Center
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
                    IconButton(
                        onClick = { /* TODO: Navegar a la IA */ },
                        // Lo alineamos a la derecha y centrado verticalmente
                        modifier = Modifier
                            //.align(Alignment.CenterEnd as Alignment.Vertical) // Esta línea daba error de casteo
                            .background(Color(0xFF0B5696), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward
                            , // Ícono de "magia" o IA
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

            // --- CALENDARIO ---
            CalendarHeader(
                currentMonth = currentMonth,
                onPrevMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            // --- SECCIÓN DE TAREAS (AHORA ES DINÁMICA) ---
            Text(
                text = when (selectedDate) {
                    LocalDate.now().minusDays(1) -> "Tareas de Ayer"
                    LocalDate.now() -> "Tareas de Hoy"
                    LocalDate.now().plusDays(1) -> "Tareas de Mañana"
                    else -> "Tareas para ${selectedDate?.dayOfMonth}/${selectedDate?.monthValue}"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B5696)
            )

            // Aquí mostrarías la lista de tareas filtrada por 'selectedDate'
            // Por ahora, dejamos una como ejemplo para mostrar cómo se vería
            TaskCard(
                title = "Tarea para el día seleccionado",
                onClick = {}
            )
        }
    }
}

// --- COMPONENTES DEL CALENDARIO ---

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Mes anterior")
        }
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}"
                .replaceFirstChar { it.uppercase() },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Mes siguiente")
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    // Ajuste para que Lunes sea el primer día (0) y Domingo el último (6)
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Encabezados de los días de la semana (L, M, X...)
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Genera las filas de días
        val totalCells = (daysInMonth + firstDayOfMonth)
        val numRows = (totalCells + 6) / 7

        for (row in 0 until numRows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    if (index >= firstDayOfMonth && index < totalCells) {
                        val day = index - firstDayOfMonth + 1
                        val date = currentMonth.atDay(day)
                        DayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            onClick = { onDateSelected(date) }
                        )
                    } else {
                        // Celdas vacías para rellenar
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.DayCell(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()
    val backgroundColor = when {
        isSelected -> Color(0xFF0B5696) // Azul para el seleccionado
        isToday -> Color(0xFFD3E6F8)      // Azul claro para hoy
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected -> Color.White
        isToday -> Color(0xFF0B5696)
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f) // Hace que la celda sea cuadrada
            .padding(2.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}
