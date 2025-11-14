package me.uflow.unab.edu.uflow.data.repository

import androidx.compose.ui.graphics.Color
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

data class NivelInfo(
    val nivel: Nivel,
    val emoji: String,
    val titulo: String,
    val subtitulo: String,
    val color: Color,
    val chips: List<Pair<String, String>>
)

object RoadmapRepository {

    private val colorCero = Color(0xFF75A95B)
    private val colorPrincipiante = Color(0xFF4A89A6)
    private val colorIntermedio = Color(0xFFB878D3)
    private val colorAvanzado = Color(0xFFE04F4C)
    
    fun getRoadmap(lenguaje: Lenguaje): List<NivelInfo> {
        when (lenguaje) {
            Lenguaje.PYTHON -> return listOf(
                NivelInfo(
                    Nivel.DESDE_CERO, "🌱", "Desde 0", "(Nivel inicial – para principiantes absolutos)", colorCero,
                    listOf("📚" to "Conceptos básicos", "🖥️" to "Entorno (VSCode)", "🐍" to "Variables y Tipos", "🖨️" to "Print y Inputs", "➕" to "Operadores")
                ),
                NivelInfo(
                    Nivel.PRINCIPIANTE, "🛠️", "Principiante", "(Primeros pasos en programación con Python)", colorPrincipiante,
                    listOf("🔀" to "Control de flujo (if/else)", "🔄" to "Bucles (For/While)", "⚙️" to "Funciones", "📊" to "Listas y Tuplas", "📖" to "Diccionarios")
                ),
                NivelInfo(
                    Nivel.INTERMEDIO, "🚀", "Intermedio", "(Dominio de conceptos clave y POO)", colorIntermedio,
                    listOf("🏗️" to "POO (Clases/Objetos)", "📦" to "Módulos y Paquetes (pip)", "📂" to "Manejo de Archivos", "🧮" to "Manejo de Errores", "🧩" to "List Comprehensions")
                ),
                NivelInfo(
                    Nivel.AVANZADO, "⚡", "Avanzado", "(Nivel profesional – proyectos reales)", colorAvanzado,
                    listOf("🌐" to "APIs (Requests/Flask)", "🔬" to "Intro a Ciencia de Datos", "🤖" to "Automatización", "🧪" to "Testing (PyTest)", "🟣" to "Proyecto Final")
                )
            )

            Lenguaje.JAVA -> return listOf(
                NivelInfo(
                    Nivel.DESDE_CERO, "🌱", "Desde 0", "(Nivel inicial – para principiantes absolutos)", colorCero,
                    listOf("☕" to "Qué es JVM", "🖥️" to "Entorno (IntelliJ)", "📦" to "Sintaxis Básica", "🖨️" to "System.out.println", "➕" to "Operadores")
                ),
                NivelInfo(
                    Nivel.PRINCIPIANTE, "🛠️", "Principiante", "(Primeros pasos en programación con Java)", colorPrincipiante,
                    listOf("🔀" to "Control de flujo (if/else)", "🔄" to "Bucles (For/While)", "⚙️" to "Métodos", "📊" to "Arrays", "📜" to "Clase String")
                ),
                NivelInfo(
                    Nivel.INTERMEDIO, "🚀", "Intermedio", "(Dominio de conceptos clave y POO)", colorIntermedio,
                    listOf("🏗️" to "POO (Clases, Herencia)", "🧩" to "Interfaces", "📚" to "Colecciones (List, Map)", "🧮" to "Manejo de Excepciones", "📂" to "Manejo de Archivos")
                ),
                NivelInfo(
                    Nivel.AVANZADO, "⚡", "Avanzado", "(Nivel profesional – proyectos reales)", colorAvanzado,
                    listOf("🧬" to "Genéricos", "🧵" to "Hilos (Threads)", "🏞️" to "Streams API", "🌐" to "Intro a Spring Boot", "🟣" to "Proyecto Final")
                )
            )

            Lenguaje.KOTLIN -> return listOf(
                NivelInfo(
                    Nivel.DESDE_CERO, "🌱", "Desde 0", "(Nivel inicial – para principiantes absolutos)", colorCero,
                    listOf("📦" to "Variables (val/var)", "🖥️" to "Entorno (IntelliJ)", "❔" to "Null Safety", "⚙️" to "Funciones Básicas", "🖨️" to "Println")
                ),
                NivelInfo(
                    Nivel.PRINCIPIANTE, "🛠️", "Principiante", "(Primeros pasos en programación con Kotlin)", colorPrincipiante,
                    listOf("🔀" to "Control (if/when)", "🔄" to "Bucles (For/While)", "📚" to "Colecciones (List, Map)", "🧩" to "Extension Functions", "📜" to "Data Classes")
                ),
                NivelInfo(
                    Nivel.INTERMEDIO, "🚀", "Intermedio", "(Dominio de conceptos clave y POO)", colorIntermedio,
                    listOf("🏗️" to "POO (Clases, Herencia)", "🧬" to "Interfaces", "🧮" to "Manejo de Errores", "🟰" to "Lambdas y HOFTs", "📱" to "Intro a Android")
                ),
                NivelInfo(
                    Nivel.AVANZADO, "⚡", "Avanzado", "(Nivel profesional – proyectos reales)", colorAvanzado,
                    listOf("🕰️" to "Coroutines (Básicos)", "📦" to "Scope Functions", "🌐" to "Intro a Ktor (Backend)", "🧪" to "Testing (JUnit)", "🟣" to "Proyecto Final")
                )
            )

            Lenguaje.WEB -> return listOf(
                NivelInfo(
                    Nivel.DESDE_CERO, "🌱", "Desde 0", "(Nivel inicial – El trío fundamental)", colorCero,
                    listOf("📝" to "Qué es HTML", "🎨" to "Qué es CSS", "💡" to "Qué es JavaScript", "🏗️" to "Estructura HTML", "🖌️" to "CSS Básico (color, font)")
                ),
                NivelInfo(
                    Nivel.PRINCIPIANTE, "🛠️", "Principiante", "(Primeros pasos en maquetación y JS)", colorPrincipiante,
                    listOf("📦" to "CSS Box Model", "📱" to "Flexbox", "⚙️" to "JS (Variables, Funciones)", "🖱️" to "Eventos DOM", "📜" to "Formularios HTML")
                ),
                NivelInfo(
                    Nivel.INTERMEDIO, "🚀", "Intermedio", "(Dominio de JS moderno y frameworks)", colorIntermedio,
                    listOf("ES6+" to "JS Moderno", "🌐" to "Fetch API (APIs)", "🎨" to "CSS Grid", "📱" to "Responsive Design", "⚛️" to "Intro a React/Vue")
                ),
                NivelInfo(
                    Nivel.AVANZADO, "⚡", "Avanzado", "(Nivel profesional – fullstack)", colorAvanzado,
                    listOf("⚙️" to "React Hooks / Vue 3", "💾" to "State Management", "🧭" to "Routing", "🖥️" to "Intro a Node.js (Backend)", "🟣" to "Proyecto Fullstack")
                )
            )
        }
    }
}