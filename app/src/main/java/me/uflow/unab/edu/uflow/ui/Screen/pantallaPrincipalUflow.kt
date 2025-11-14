package me.uflow.unab.edu.uflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import me.uflow.unab.edu.uflow.R

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.uflow.unab.edu.uflow.ui.viewmodel.AuthViewModel

@Composable
fun PantallaMaquetacionInicioInlineTotal(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(220.dp))

        Image(
            painter = painterResource(id = R.drawable.logouflow), 
            contentDescription = "Logo de UFlow",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        
        Spacer(modifier = Modifier.height(200.dp))
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            //Botón INICIAR SESIÓN
            Button(
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D4791)),
                shape = MaterialTheme.shapes.medium.copy(
                    all = androidx.compose.foundation.shape.CornerSize(13.dp)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Botón REGISTRARSE
            OutlinedButton(
                onClick = onNavigateToRegister,
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    all = androidx.compose.foundation.shape.CornerSize(13.dp)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF3D4791),
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color(0xFF3D4791),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                )) {
                    append("Contactanos")
                }
            },
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 40.dp)
                .clickable { /* TODO: */ },
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
    }
}
