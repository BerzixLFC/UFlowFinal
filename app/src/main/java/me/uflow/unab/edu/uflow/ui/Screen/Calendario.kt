package me.uflow.unab.edu.uflow.ui.Screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.ui.components.TaskCard
import me.uflow.unab.edu.uflow.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// Se mantiene el Saver para LocalDate
val LocalDateSaver = Saver<LocalDate, String>(
    save = { it.toString() },
    restore = { LocalDate.parse(it) }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendario(
    navController: NavController,
    onBack: () -> Unit = {},
    taskViewModel: TaskViewModel
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by rememberSaveable(stateSaver = LocalDateSaver) { mutableStateOf(LocalDate.now()) }
    val tasksForDay by taskViewModel.tasks.collectAsState()

    // Este LaunchedEffect es sensible a cambios en selectedDate.
    // Si la UI es estable, solo se ejecutará cuando DEBE.
    LaunchedEffect(selectedDate) {
        println("LaunchedEffect disparado para la fecha: $selectedDate")
        taskViewModel.loadTasksForDate(selectedDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.botonatras),
                            contentDescription = "Regresar",
                            modifier = Modifier.size(45.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    // MODIFICADO: Se elimina .Companion
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically // Añadido para mejor alineación
                ) {
                    Button(
                        onClick = {
                            navController.navigate("taskDetail/${selectedDate.toString()}")
                        },
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFAFAFA)),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Crear una tarea",
                            fontSize = 28.sp,
                            // MODIFICADO: Se elimina .Companion
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF484848)
                        )
                    }
                    Spacer(Modifier.size(16.dp)) // Añadido para separar los botones
                    IconButton(
                        onClick = { /* TODO: Navegar a la IA */ },
                        // MODIFICADO: Se elimina .Companion
                        modifier = Modifier
                            .background(Color(0xFF0B5696), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Ir a la IA",
                            // MODIFICADO: Se elimina .Companion
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            // MODIFICADO: Se elimina .Companion de todos los Modifiers
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Text(
                text = when (selectedDate) {
                    LocalDate.now().minusDays(1) -> "Tareas de Ayer"
                    LocalDate.now() -> "Tareas de Hoy"
                    LocalDate.now().plusDays(1) -> "Tareas de Mañana"
                    else -> "Tareas para ${selectedDate.dayOfMonth}/${selectedDate.monthValue}"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B5696)
            )

            // La lógica para mostrar tareas o el texto vacío ya era correcta.
            if (tasksForDay.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No tienes tareas para este día.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            } else {
                LazyColumn (
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(tasksForDay) { task ->
                        TaskCard(
                            title = task.title,
                            onClick = {
                                // Navegamos con el ID del documento, que es más seguro.
                                navController.navigate("taskDetail/${task.id}")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        // MODIFICADO: Se elimina .Companion
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
            text = "${
                currentMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${currentMonth.year}"
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
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
        isSelected -> Color(0xFF0B5696)
        isToday -> Color(0xFFD3E6F8)
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
            .aspectRatio(1f)
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

