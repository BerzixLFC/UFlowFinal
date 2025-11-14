package me.uflow.unab.edu.uflow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.isNotEmpty
import me.uflow.unab.edu.uflow.util.isValidEmail
import me.uflow.unab.edu.uflow.util.isValidPassword
import me.uflow.unab.edu.uflow.util.passwordsMatch

@Composable
fun RoundedTopCard(
    topRadius: Dp = 30.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = topRadius,
                    topEnd = topRadius
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

//Pantalla Principal de Registro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroUflowHardcode(
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    //Estado Local para los Campos del Formulario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") } // <-- ¡Este campo es clave!
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estado Local para las Validaciones
    var isNombreError by remember { mutableStateOf(false) }
    var isApellidoError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isUsuarioError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isConfirmPasswordError by remember { mutableStateOf(false) }

    val passwordsDoMatch = password.isNotEmpty() && passwordsMatch(password, confirmPassword)

    // Observa el estado del ViewModel (isLoading, error)
    val authState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efecto para mostrar Toasts de error desde el ViewModel
    LaunchedEffect(authState.error) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError() // Limpia el error después de mostrarlo
        }
    }

    // Función de Validación Local
    fun validateFields(): Boolean {
        isNombreError = !isNotEmpty(nombre)
        isApellidoError = !isNotEmpty(apellido)
        isEmailError = !isValidEmail(email)
        isUsuarioError = !isNotEmpty(usuario)
        isPasswordError = !isValidPassword(password)
        isConfirmPasswordError = !passwordsMatch(password, confirmPassword)

        // Devuelve true si NINGÚN campo tiene error
        return !isNombreError && !isApellidoError && !isEmailError && !isUsuarioError && !isPasswordError && !isConfirmPasswordError
    }

    // Estilos locales
    val cornerRadius = 12.dp
    val fieldFontSize = 15.sp
    val customTextStyle = TextStyle(fontSize = fieldFontSize, color = Color(0xFF666666))

    //UI Layout
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_register),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //... (Título)
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Únete a",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Uflow",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            //Tarjeta Blanca con el Formulario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                RoundedTopCard(topRadius = 13.dp) {
                    //Campos del Formulario
                    FormFieldTitle("Tu nombre")
                    NameFields(
                        nombre = nombre,
                        onNombreChange = {
                            nombre = it; isNombreError = false
                        },
                        apellido = apellido,
                        onApellidoChange = { apellido = it; isApellidoError = false },
                        textStyle = customTextStyle,
                        cornerRadius = cornerRadius,
                        isNombreError = isNombreError,
                        isApellidoError = isApellidoError
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    FormFieldTitle("Correo Electronico")
                    EmailField(
                        email = email,
                        onEmailChange = { email = it; isEmailError = false },
                        textStyle = customTextStyle,
                        cornerRadius = cornerRadius,
                        isError = isEmailError
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    FormFieldTitle("Usuario")
                    UserField(
                        usuario = usuario,
                        onUserChange = { usuario = it; isUsuarioError = false },
                        textStyle = customTextStyle,
                        cornerRadius = cornerRadius,
                        isError = isUsuarioError
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    FormFieldTitle("Fecha de Nacimiento")
                    DateDropDowns(
                        fontSize = fieldFontSize,
                        cornerRadius = cornerRadius
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    FormFieldTitle("Contraseña")
                    PasswordField(
                        password = password,
                        onPasswordChange = {
                            password = it
                            isPasswordError = false
                            if (isConfirmPasswordError) {
                                isConfirmPasswordError = !passwordsMatch(it, confirmPassword)
                            }
                        },
                        textStyle = customTextStyle,
                        cornerRadius = cornerRadius,
                        placeholder = "Mínimo 6 caracteres",
                        showCheck = false,
                        isError = isPasswordError
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    FormFieldTitle("Confirmar contraseña")
                    PasswordField(
                        password = confirmPassword,
                        onPasswordChange = {
                            confirmPassword = it
                            isConfirmPasswordError = !passwordsMatch(password, it)
                        },
                        textStyle = customTextStyle,
                        cornerRadius = cornerRadius,
                        placeholder = "Confirma tu contraseña",
                        showCheck = passwordsDoMatch,
                        isError = isConfirmPasswordError
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    //Botón Crear Cuenta
                    Button(
                        onClick = {
                            if (validateFields()) {
                                viewModel.createUserWithEmail(
                                    email,
                                    password,
                                    nombre,
                                    apellido,
                                    usuario
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor, corrige los campos en rojo",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        enabled = !authState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D4791))
                    ) {
                        if (authState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Crear Cuenta",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    //Link para volver al Login
                    Text(
                        text = "Volver al Login",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3D4791),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToLogin() }
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FormFieldTitle(text: String) {
    Text(
        text = text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun NameFields(
    nombre: String, onNombreChange: (String) -> Unit,
    apellido: String, onApellidoChange: (String) -> Unit,
    textStyle: TextStyle, cornerRadius: Dp,
    isNombreError: Boolean, isApellidoError: Boolean
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = { Text("Nombre", color = Color(0xFFAAAAAA), fontSize = 15.sp) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(cornerRadius),
            textStyle = textStyle,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3D4791),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            isError = isNombreError
        )
        OutlinedTextField(
            value = apellido,
            onValueChange = onApellidoChange,
            placeholder = { Text("Apellido", color = Color(0xFFAAAAAA), fontSize = 15.sp) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(cornerRadius),
            textStyle = textStyle,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3D4791),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            isError = isApellidoError
        )
    }
}

@Composable
private fun EmailField(
    email: String, onEmailChange: (String) -> Unit,
    textStyle: TextStyle, cornerRadius: Dp,
    isError: Boolean
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = {
            Text(
                "Tu correo electronico",
                color = Color(0xFFAAAAAA),
                fontSize = 15.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        textStyle = textStyle,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3D4791),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        ),
        isError = isError
    )
}

@Composable
private fun UserField(
    usuario: String, onUserChange: (String) -> Unit,
    textStyle: TextStyle, cornerRadius: Dp,
    isError: Boolean
) {
    OutlinedTextField(
        value = usuario,
        onValueChange = onUserChange,
        placeholder = { Text("Ejemplo123", color = Color(0xFFAAAAAA), fontSize = 15.sp) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        textStyle = textStyle,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3D4791),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        ),
        isError = isError
    )
}

@Composable
private fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    placeholder: String,
    textStyle: TextStyle,
    cornerRadius: Dp,
    showCheck: Boolean,
    isError: Boolean
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        placeholder = { Text(placeholder, color = Color(0xFFAAAAAA), fontSize = 15.sp) },
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
            if (showCheck) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Contraseñas coinciden",
                    tint = Color(0xFF4CAF50) // Verde
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        textStyle = textStyle,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3D4791),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        ),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDropDowns(fontSize: androidx.compose.ui.unit.TextUnit, cornerRadius: Dp) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear downTo 1900).map { it.toString() }
    val months = (1..12).map { "%02d".format(it) }
    val days = (1..31).map { "%02d".format(it) }

    var selectedDay by remember { mutableStateOf("Día") }
    var selectedMonth by remember { mutableStateOf("Mes") }
    var selectedYear by remember { mutableStateOf("Año") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DropdownField(
            days,
            selectedDay,
            { selectedDay = it },
            "Día",
            Modifier.weight(1f),
            cornerRadius,
            fontSize
        )
        DropdownField(
            months,
            selectedMonth,
            { selectedMonth = it },
            "Mes",
            Modifier.weight(1f),
            cornerRadius,
            fontSize
        )
        DropdownField(
            years,
            selectedYear,
            { selectedYear = it },
            "Año",
            Modifier.weight(1f),
            cornerRadius,
            fontSize
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
    cornerRadius: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selected == label) label else selected

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = label, color = Color(0xFFAAAAAA), fontSize = fontSize) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(cornerRadius),
            textStyle = TextStyle(
                fontSize = fontSize,
                color = if (selected == label) Color(0xFFAAAAAA) else Color.Black
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3D4791),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = fontSize) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    PantallaRegistroUflowHardcode(onNavigateToLogin = {})
}