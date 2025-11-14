package me.uflow.unab.edu.uflow.ui.Screen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.api.ApiException
import me.uflow.unab.edu.uflow.R
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel
import me.uflow.unab.edu.uflow.util.isValidEmail
import me.uflow.unab.edu.uflow.util.isValidPassword

private data class LanguageInfo(val name: String, val color: Color)

// Lista para la ruleta animada
private val lenguajesRuleta = listOf(
    LanguageInfo("Python", Color(0xFF3776AB)),
    LanguageInfo("Java", Color(0xFFF89820)),
    LanguageInfo("Kotlin", Color(0xFF7F52FF)),
    LanguageInfo("HTML", Color(0xFFE34F26)),
    LanguageInfo("CSS", Color(0xFF264DE4)),
    LanguageInfo("JavaScript", Color(0xFFF7DF1E))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLoginCompleta(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToContact: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    // ESTADO LOCAL DE LA VISTA
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }

    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    //ESTADO DEL VIEWMODEL (ahora refactorizado)
    val authState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    // Efecto para mostrar Toasts de error desde el ViewModel
    LaunchedEffect(authState.error) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError() // Limpia el error después de mostrarlo
        }
    }

    //FUNCIÓN DE VALIDACIÓN (ahora usa las funciones de 'util')
    fun validateFields(): Boolean {
        isEmailError = !isValidEmail(email)
        isPasswordError = !isValidPassword(password)
        return !isEmailError && !isPasswordError
    }

    //CONFIGURACIÓN DE GOOGLE SIGN-IN
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    val googleSignInLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken!!
                    // Llama al ViewModel refactorizado
                    viewModel.signInWithGoogleToken(idToken)
                } catch (e: ApiException) {
                    Log.w("PantallaLoginCompleta", "Google sign in failed", e)
                    Toast.makeText(
                        context,
                        "Falló el inicio de sesión con Google",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "Inicio de sesión con Google cancelado", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    fun launchGoogleSignIn() {
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    //Animación de la ruleta de texto
    val transition = rememberInfiniteTransition(label = "LanguageSwitcher");
    val index by transition.animateValue(
        initialValue = 0,
        targetValue = lenguajesRuleta.size,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 10000,
                easing = LinearEasing
            ), repeatMode = RepeatMode.Restart
        ),
        label = "LanguageIndex"
    );
    val currentLanguageInfo = lenguajesRuleta[index % lenguajesRuleta.size]

    //UI LAYOUT
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //Header y Título Animado
        Spacer(modifier = Modifier.height(50.dp));

        Text(
            text = "Bienvenido",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        );
        Text(
            text = buildAnnotatedString {
                append("a "); withStyle(
                style = SpanStyle(
                    color = Color(0xFF3D4791),
                    fontWeight = FontWeight.ExtraBold,
                )
            ) { append("Uflow") }
            },
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        );

        Spacer(modifier = Modifier.height(20.dp));

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tu camino para aprender ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                ); AnimatedContent(
                targetState = currentLanguageInfo,
                transitionSpec = {
                    (slideInVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearEasing
                        )
                    ) { fullHeight -> fullHeight } + fadeIn(animationSpec = tween(durationMillis = 500))).togetherWith(
                        slideOutVertically(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing
                            )
                        ) { fullHeight -> -fullHeight } + fadeOut(animationSpec = tween(durationMillis = 500)))
                },
                label = "LanguageContent"
            ) { targetLanguageInfo ->
                Text(
                    text = targetLanguageInfo.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = targetLanguageInfo.color,
                    fontFamily = FontFamily.SansSerif
                )
            }
            }; Text(
            text = "comienza aquí",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
        }; Spacer(modifier = Modifier.height(30.dp))

        // Campos de Texto (Formulario)
        Text(
            text = "Correo Electronico",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        ); Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; isEmailError = false },
            placeholder = { Text("Ejemplo@email.com") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(),
            isError = isEmailError
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        ); Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; isPasswordError = false },
            placeholder = { Text("Tu Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(),
            isError = isPasswordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = rememberMe,
                onClick = { rememberMe = !rememberMe },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF3D4791),
                    unselectedColor = Color.Gray
                ),
                modifier = Modifier.size(24.dp)
            ); Text(
            text = "Recuérdame", // <-- Corregido
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        ); Spacer(modifier = Modifier.weight(1f))
        }; Spacer(modifier = Modifier.height(20.dp))

        //Botón de Iniciar Sesión (Email)
        Button(
            onClick = {
                if (validateFields()) {
                    viewModel.loginUserWithEmail(email, password)
                }
            },
            enabled = !authState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D4791)),
            shape = MaterialTheme.shapes.medium.copy(
                all = CornerSize(
                    13.dp
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                append("¿Olvidaste tu contraseña? "); withStyle(
                style = SpanStyle(
                    color = Color(0xFF3D4791),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            ) { append("¡Click aquí!") }
            },
            color = Color.Black.copy(alpha = 0.7f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { onNavigateToForgotPassword() }); Spacer(
        modifier = Modifier.height(
            12.dp
        )
    ); Text(
        text = buildAnnotatedString {
            append("¿No tienes cuenta? "); withStyle(
            style = SpanStyle(
                color = Color(0xFF3D4791),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        ) { append("Regístrate") }
        },
        color = Color.Black.copy(alpha = 0.7f),
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        modifier = Modifier.clickable { onNavigateToRegister() }); Spacer(
        modifier = Modifier.height(
            30.dp
        )
    )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                color = Color.LightGray,
                modifier = Modifier.weight(1f),
                thickness = 1.dp
            ); Text(
            text = " O conéctate con ", // <-- Corregido
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.Black,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif
        ); Divider(
            color = Color.LightGray,
            modifier = Modifier.weight(1f),
            thickness = 1.dp
        )
        }; Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Botón Google
            Button(
                onClick = { launchGoogleSignIn() },
                enabled = !authState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F1F1),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Logo de Google",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Botón GitHub
            Button(
                onClick = {
                    val activity = context as? Activity; if (activity != null) {
                    viewModel.signInWithGitHub(activity)
                } else {
                    Toast.makeText(context, "No se pudo obtener la actividad.", Toast.LENGTH_SHORT)
                        .show()
                }
                },
                enabled = !authState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F1F1),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.github_logo),
                    contentDescription = "Logo de GitHub",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f)); Text(
        text = buildAnnotatedString {
            append("¿Necesitas ayuda? "); withStyle(
            style = SpanStyle(
                color = Color(0xFF3D4791),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        ) { append("Contáctanos") } 
        },
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier
            .padding(bottom = 40.dp)
            .clickable { onNavigateToContact() },
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold
    )
    }
}