package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.CountryList
import java.util.Calendar

private fun parseDate(dateStr: String): Triple<String, String, Int> {
    if (dateStr.isBlank() || dateStr == "Oculto") {
        return Triple("Día", "Mes", Calendar.getInstance().get(Calendar.YEAR))
    }
    val parts = dateStr.split("-")
    return try {
        val day = parts[0]
        val month = parts[1]
        val year = parts[2].toInt()
        Triple(day, month, year)
    } catch (e: Exception) {
        Triple("Día", "Mes", Calendar.getInstance().get(Calendar.YEAR))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarInformacion(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    val userData = uiState.userData

    var nombre by remember(userData) { mutableStateOf(userData?.nombre ?: "") }
    var apellido by remember(userData) { mutableStateOf(userData?.apellido ?: "") }
    var usuario by remember(userData) { mutableStateOf(userData?.usuario ?: "") }
    var localizacion by remember(userData) { mutableStateOf(userData?.localizacion ?: "") }

    //Lógica de Cumpleaños
    val initialDate = parseDate(userData?.cumpleanos ?: "")
    var selectedDay by remember(userData) { mutableStateOf(initialDate.first) }
    var selectedMonth by remember(userData) { mutableStateOf(initialDate.second) }
    var selectedYear by remember(userData) { mutableStateOf(initialDate.third.toString()) }
    var isOculto by remember(userData) { mutableStateOf(userData?.cumpleanos == "Oculto") }

    var localizacionExpanded by remember { mutableStateOf(false) }
    val countries = remember { CountryList.countries }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Información") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nombre de Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("@") }
            )

            Text(
                "Cumpleaños",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DateDropDowns(
                day = selectedDay,
                month = selectedMonth,
                year = selectedYear,
                onDayChange = { selectedDay = it },
                onMonthChange = { selectedMonth = it },
                onYearChange = { selectedYear = it },
                isEnabled = !isOculto // Si está oculto, no se puede editar
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isOculto = !isOculto }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = isOculto,
                    onCheckedChange = { isOculto = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ocultar cumpleaños en mi perfil")
            }

            ExposedDropdownMenuBox(
                expanded = localizacionExpanded,
                onExpandedChange = { localizacionExpanded = !localizacionExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = localizacion,
                    onValueChange = { localizacion = it },
                    label = { Text("Localización") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = localizacionExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                val filteredCountries = countries.filter {
                    it.contains(localizacion, ignoreCase = true)
                }

                if (filteredCountries.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = localizacionExpanded,
                        onDismissRequest = { localizacionExpanded = false }
                    ) {
                        filteredCountries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                onClick = {
                                    localizacion = country
                                    localizacionExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val cumpleanosString = if (isOculto) {
                        "Oculto"
                    } else if (selectedDay != "Día" && selectedMonth != "Mes" && selectedYear != "Año") {
                        "$selectedDay-$selectedMonth-$selectedYear"
                    } else {
                        ""
                    }
                    authViewModel.updateUserProfileInfo(
                        nombre.trim(),
                        apellido.trim(),
                        usuario.trim(),
                        cumpleanosString,
                        localizacion.trim()
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


//COMPOSABLES PRIVADOS COPIADOS DE registerUflow.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDropDowns(
    day: String,
    month: String,
    year: String,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    isEnabled: Boolean
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear downTo 1900).map { it.toString() }
    val months = listOf(
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    )
    val days = (1..31).map { it.toString() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DropdownField(
            items = days,
            selected = day,
            onSelected = onDayChange,
            label = "Día",
            modifier = Modifier.weight(1f),
            isEnabled = isEnabled
        )
        DropdownField(
            items = months,
            selected = month,
            onSelected = onMonthChange,
            label = "Mes",
            modifier = Modifier.weight(1.5f),
            isEnabled = isEnabled
        )
        DropdownField(
            items = years,
            selected = year,
            onSelected = onYearChange,
            label = "Año",
            modifier = Modifier.weight(1.2f),
            isEnabled = isEnabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    label: String,
    modifier: Modifier,
    isEnabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selected == label) label else selected

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (isEnabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            placeholder = { Text(text = label, color = Color(0xFFAAAAAA), fontSize = 15.sp) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = if (selected == label) Color(0xFFAAAAAA) else Color.Black
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                disabledBorderColor = Color(0xFFE0E0E0).copy(alpha = 0.5f),
                disabledTextColor = Color.Gray.copy(alpha = 0.5f),
                disabledPlaceholderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = 15.sp) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}