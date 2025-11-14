package me.uflow.unab.edu.uflow.util

import androidx.compose.ui.graphics.Color
import me.uflow.unab.edu.uflow.R // Asegúrate de que R se importe correctamente

enum class Lenguaje(val nombre: String, val color: Color, val iconResId: Int) {
    PYTHON("Python", Color(0xFF3177AB), R.drawable.python_icon_menu),
    JAVA("Java", Color(0xFFED8C09), R.drawable.javabase_logo),
    KOTLIN("Kotlin", Color(0xFF8A7AB8), R.drawable.kotlin_icon_menu),
    WEB("Desarrollo Web", Color(0xFF861818), R.drawable.web_dev_menu)
}

enum class Nivel(val displayName: String) {
    DESDE_CERO("Desde Cero"),
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado")
}