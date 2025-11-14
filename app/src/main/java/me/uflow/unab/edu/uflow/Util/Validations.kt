package me.uflow.unab.edu.uflow.util

import android.util.Patterns

// Verifica si un campo de texto no está vacío
fun isNotEmpty(text: String): Boolean {
    return text.trim().isNotEmpty()
}

// Verifica si el email tiene un formato válido.
fun isValidEmail(email: String): Boolean {
    return isNotEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Verifica si la contraseña tiene 6 caracteres o más.
fun isValidPassword(password: String): Boolean {
    return isNotEmpty(password) && password.length >= 6
}

// Verifica si dos contraseñas coinciden.
fun passwordsMatch(pass1: String, pass2: String): Boolean {
    return pass1 == pass2
}