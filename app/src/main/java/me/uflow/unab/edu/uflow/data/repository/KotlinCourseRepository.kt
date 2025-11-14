package me.uflow.unab.edu.uflow.data.repository

import me.uflow.unab.edu.uflow.data.model.CourseDetails
import me.uflow.unab.edu.uflow.data.model.LessonStep
import me.uflow.unab.edu.uflow.data.model.Session
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

//REPOSITORY DE TODOS LOS CURSOS DE KOTLIN
//NO TIENE CURSOS PAIR PROGRAMMING
//CURSOS HECHOS CON IA

object KotlinCourseRepository {

    // --- Esta sesión ahora solo tiene 1 concepto y 1 pregunta ---
    private val k_intro_s1_steps = listOf(
        LessonStep.Concept(
            id = 1, title = "¡Bienvenido a Kotlin!",
            body = "Kotlin es un lenguaje de programación moderno, conciso y seguro, famoso por ser el lenguaje oficial para el desarrollo de Android.\n\nEn esta serie de cursos, aprenderás todo lo necesario para dominarlo."
        ),
        LessonStep.FillBlank(
            id = 2,
            codeSnippet = "¡Estás a punto de empezar tu viaje!\n\n¿Estás listo para iniciar?",
            options = listOf("¡Sí, vamos!", "No, aún no"),
            correctAnswerIndex = 0 // El usuario debe presionar "Sí" para aprobar
        )
    )

    private val kotlin_intro = CourseDetails(
        id = "kotlin_intro",
        title = "Bienvenido a Kotlin", // <-- Título actualizado
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO, // Nivel "Desde Cero"
        sessions = listOf(
            Session(
                "k_intro_s1",
                "1. ¡Bienvenido!", // <-- Título de sesión actualizado
                "Una introducción rápida a tu viaje.",
                k_intro_s1_steps
            )
        )
    )

    // =================================================================================
    // --- NIVEL 1: DESDE CERO (ID: kotlin_cero) ---
    // =================================================================================

    // --- Sesión 1: Variables y Tipos de Datos (8 Pasos / 5 Preguntas) ---
    private val k0_s1_steps = listOf(
        LessonStep.Concept(
            id = 1, title = "Variables: val vs. var",
            body = "En Kotlin, declaramos variables usando dos palabras clave:\n\n" +
                    "• `val`: Es para valores **inmutables** (solo lectura). Una vez que le asignas un valor, no puedes cambiarlo.\n\n" +
                    "• `var`: Es para valores **mutables** (variables). Puedes cambiar su valor después de asignarlo."
        ),
        LessonStep.FillBlank(
            id = 2, codeSnippet = "Queremos guardar un nombre que no cambiará.\n\n`___` nombre = \"Gabriel\"",
            options = listOf("val", "var", "fun"), correctAnswerIndex = 0
        ),
        LessonStep.FindError(
            id = 3, codeLines = listOf("val numero = 10", "numero = 15"),
            correctLineIndex = 1
        ),
        LessonStep.Concept(
            id = 4, title = "Tipos de Datos Básicos",
            body = "Kotlin es inteligente y puede *inferir* el tipo de dato, pero estos son los más comunes:\n\n" +
                    "• `String`: Texto (ej. \"Hola\")\n" +
                    "• `Int`: Números enteros (ej. 10, -5)\n" +
                    "• `Boolean`: Verdadero o falso (ej. true, false)"
        ),
        LessonStep.PredictResult(
            id = 5, codeSnippet = "val x = \"10\"\nval y = 20\nprint(x + y)",
            options = listOf("30", "1020", "Error"), correctAnswerIndex = 1
        ),
        LessonStep.FillBlank(
            id = 6, codeSnippet = "Queremos guardar una edad que cambiará cada año.\n\n`___` edad = 25",
            options = listOf("val", "const", "var"), correctAnswerIndex = 2
        ),
        LessonStep.Concept(
            id = 7, title = "Plantillas de Strings",
            body = "Puedes insertar variables directamente dentro de un String usando el símbolo `$`.\n\n" +
                    "Ejemplo:\n`val nombre = \"Ana\"`\n`print(\"Hola, \$nombre\")`"
        ),
        LessonStep.PredictResult(
            id = 8, codeSnippet = "val puntos = 100\nprint(\"Ganaste \$puntos puntos!\")",
            options = listOf("Ganaste 100 puntos!", "Ganaste \$puntos puntos!", "Error"), correctAnswerIndex = 0
        )
    )

    // --- Sesión 2: Entorno de Desarrollo (5 Pasos / 3 Preguntas) ---
    private val k0_s2_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es un IDE?", body = "Un IDE (Entorno de Desarrollo Integrado) es un programa que te da todas las herramientas para escribir, probar y ejecutar código. Para Kotlin, usaremos **IntelliJ IDEA**."),
        LessonStep.Concept(id = 2, title = "Instalando IntelliJ IDEA", body = "JetBrains, los creadores de Kotlin, también hacen IntelliJ. Puedes descargar la 'Community Edition' (es gratis) desde su sitio web oficial."),
        LessonStep.FillBlank(id = 3, codeSnippet = "El IDE oficial para desarrollo Kotlin, hecho por JetBrains, se llama:\n\n`IntelliJ `_____", options = listOf("Studio", "Code", "IDEA"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 4, title = "Creando un Proyecto", body = "Cuando creas un nuevo proyecto en IntelliJ, puedes seleccionar 'Kotlin'. Asegúrate de elegir la JVM (Java Virtual Machine), ya que Kotlin corre sobre ella."),
        LessonStep.FindError(id = 5, codeLines = listOf("// Tu primer proyecto", "seleccionas 'Kotlin'", "seleccionas 'Android' // <-- Error para un proyecto simple", "seleccionas 'JVM'"), correctLineIndex = 2)
    )

    // --- Sesión 3: Hola Mundo y Println (5 Pasos / 3 Preguntas) ---
    private val k0_s3_steps = listOf(
        LessonStep.Concept(id = 1, title = "La función 'main'", body = "Todo programa de Kotlin necesita un punto de entrada. Esta es la función `main`.\n\n`fun main() {`\n`  // El código empieza aquí`\n`}`"),
        LessonStep.Concept(id = 2, title = "Imprimiendo en Consola", body = "Para mostrar texto en la consola (la ventana de resultados), usamos la función `println()`.\n\n`println(\"Hola Mundo\")`"),
        LessonStep.FillBlank(id = 3, codeSnippet = "Para imprimir 'Uflow' en la consola:\n\n`_______(\"Uflow\")`", options = listOf("print", "log", "println"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 4, title = "Diferencia: print vs println", body = "`print()` imprime el texto.\n`println()` imprime el texto y luego añade un *salto de línea* (line new)."),
        LessonStep.PredictResult(id = 5, codeSnippet = "print(\"Hola\")\nprint(\"Mundo\")", options = listOf("Hola\nMundo", "HolaMundo", "Error"), correctAnswerIndex = 1)
    )

    // --- Sesión 4: Operadores y Lógica (6 Pasos / 4 Preguntas) ---
    private val k0_s4_steps = listOf(
        LessonStep.Concept(
            id = 1, title = "Operadores Aritméticos",
            body = "Son los símbolos que usas para hacer matemáticas:\n\n" +
                    "• `+` (suma), `-` (resta), `*` (multiplicación), `/` (división)\n" +
                    "• `%` (módulo, el residuo de una división)"
        ),
        LessonStep.PredictResult(
            id = 2, codeSnippet = "val x = 10 % 3\nprint(x)",
            options = listOf("3", "1", "0.33"), correctAnswerIndex = 1
        ),
        LessonStep.Concept(
            id = 3, title = "Operadores de Comparación",
            body = "Devuelven un `Boolean` (true o false):\n\n" +
                    "• `==` (igual a), `!=` (diferente de)\n" +
                    "• `>` (mayor que), `<` (menor que), `>=` (mayor o igual), `<=` (menor o igual)"
        ),
        LessonStep.FillBlank(
            id = 4, codeSnippet = "Para comprobar si 'a' y 'b' son iguales, usamos:\n\n`if (a ___ b)`",
            options = listOf("=", "==", "==="), correctAnswerIndex = 1
        ),
        LessonStep.Concept(
            id = 5, title = "Operadores Lógicos",
            body = "Se usan para combinar varias condiciones `Boolean`:\n\n" +
                    "• `&&` (Y lógico): Ambas deben ser `true`.\n" +
                    "• `||` (O lógico): Al menos una debe ser `true`."
        ),
        LessonStep.PredictResult(
            id = 6, codeSnippet = "val edad = 20\nval tienePase = false\nprint(edad > 18 && tienePase)",
            options = listOf("true", "false", "Error"), correctAnswerIndex = 1
        )
    )

    // --- Sesión 5: Control de Flujo (If / When) (7 Pasos / 4 Preguntas) ---
    private val k0_s5_steps = listOf(
        LessonStep.Concept(
            id = 1, title = "La sentencia 'if'",
            body = "El `if` te permite ejecutar código solo si una condición es verdadera.\n\n" +
                    "if (condicion) {\n  // hacer algo\n} else {\n  // hacer otra cosa\n}"
        ),
        LessonStep.FindError(
            id = 2, codeLines = listOf("val x = 10", "if (x = 10) {", "  print(\"Hola\")", "}"),
            correctLineIndex = 1
        ),
        LessonStep.Concept(
            id = 3, title = "'if' como Expresión",
            body = "En Kotlin, `if` puede *devolver* un valor. Esto es muy útil para asignar variables.\n\n" +
                    "`val resultado = if (x > 5) \"Mayor\" else \"Menor\"`"
        ),
        LessonStep.PredictResult(
            id = 4, codeSnippet = "val temp = 25\nval clima = if (temp > 20) \"Cálido\" else \"Frío\"\nprint(clima)",
            options = listOf("Cálido", "Frío", "25"), correctAnswerIndex = 0
        ),
        LessonStep.Concept(
            id = 5, title = "La sentencia 'when'",
            body = "Es el reemplazo de Kotlin para el `switch`. Es mucho más poderoso.\n\n" +
                    "when (x) {\n  1 -> print(\"Es uno\")\n  2 -> print(\"Es dos\")\n  else -> print(\"Es otro\")\n}"
        ),
        LessonStep.FillBlank(
            id = 6, codeSnippet = "¿Qué palabra clave se usa si ninguna condición de 'when' se cumple?\n\n" +
                    "when (x) {\n  1 -> ... \n  `___` -> print(\"No es 1\")\n}",
            options = listOf("default", "else", "other"), correctAnswerIndex = 1
        ),
        LessonStep.PredictResult(
            id = 7, codeSnippet = "val dia = 3\nval nombreDia = when (dia) {\n  1 -> \"Lunes\"\n  2 -> \"Martes\"\n  else -> \"Otro\"\n}\nprint(nombreDia)",
            options = listOf("Lunes", "Martes", "Otro"), correctAnswerIndex = 2
        )
    )

    // --- Sesión 6: Funciones Básicas (6 Pasos / 3 Preguntas) ---
    private val k0_s6_steps = listOf(
        LessonStep.Concept(id = 1, title = "Funciones", body = "Las funciones son bloques de código reutilizables. Se definen con la palabra clave `fun`."),
        LessonStep.FillBlank(id = 2, codeSnippet = "`___` miFuncion() {\n  print(\"Hola\")\n}", options = listOf("val", "fun", "void"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 3, title = "Parámetros y Retorno", body = "Las funciones pueden recibir datos (parámetros) y devolver un resultado.\n\n`fun sumar(a: Int, b: Int): Int {`\n`  return a + b`\n`}`"),
        LessonStep.FindError(id = 4, codeLines = listOf("fun saludar(nombre) {", "  print(\"Hola, \$nombre\")", "}"), correctLineIndex = 0),
        LessonStep.PredictResult(id = 5, codeSnippet = "fun doble(x: Int): Int {\n  return x * 2\n}\nprint(doble(5))", options = listOf("5", "x * 2", "10"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 6, title = "Sintaxis Corta", body = "Si una función es solo una línea, puedes hacerla más corta:\n\n`fun doble(x: Int): Int = x * 2`")
    )

    // --- Sesión 7: Colecciones (Listas) (6 Pasos / 4 Preguntas) ---
    private val k0_s7_steps = listOf(
        LessonStep.Concept(id = 1, title = "Listas Inmutables", body = "Una `listOf()` es una colección de solo lectura. No puedes añadir ni quitar elementos después de crearla."),
        LessonStep.PredictResult(id = 2, codeSnippet = "val colores = listOf(\"Rojo\", \"Verde\")\nprint(colores[0])", options = listOf("Rojo", "Verde", "Error"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 3, title = "Listas Mutables", body = "Una `mutableListOf()` te permite añadir (`.add()`) y quitar (`.remove()`) elementos."),
        LessonStep.FillBlank(id = 4, codeSnippet = "val numeros = mutableListOf(10, 20)\n`numeros.____(30)`", options = listOf("add", "push", "new"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 5, codeLines = listOf("val frutas = listOf(\"Manzana\")", "frutas.add(\"Pera\")"), correctLineIndex = 1),
        LessonStep.PredictResult(id = 6, codeSnippet = "val nums = mutableListOf(1, 2, 3)\nnums.removeAt(1)\nprint(nums)", options = listOf("[1, 2, 3]", "[1, 3]", "[2, 3]"), correctAnswerIndex = 1)
    )

    // --- Sesión 8: Bucles (For / While) (7 Pasos / 4 Preguntas) ---
    private val k0_s8_steps = listOf(
        LessonStep.Concept(id = 1, title = "Bucle 'for'", body = "El bucle `for` se usa para iterar sobre una colección o un rango.\n\n`for (item in miLista) {`\n`  print(item)`\n`}`"),
        LessonStep.Concept(id = 2, title = "Rangos", body = "Puedes crear un rango de números fácilment. `1..3` incluye los números 1, 2 y 3."),
        LessonStep.PredictResult(id = 3, codeSnippet = "var total = 0\nfor (i in 1..3) {\n  total = total + i\n}\nprint(total)", options = listOf("3", "6", "Error"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 4, title = "Bucle 'while'", body = "El bucle `while` se repite *mientras* una condición sea verdadera.\n\n`while (condicion) {`\n`  // hacer algo`\n`}`"),
        LessonStep.FindError(id = 5, codeLines = listOf("var i = 0", "while (i < 3) {", "  print(i)", "}"), correctLineIndex = 1),
        LessonStep.FillBlank(id = 6, codeSnippet = "Iterar sobre una lista de frutas:\n\n`for (fruta `__` frutas) { ... }`", options = listOf(":", "in", "of"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 7, title = "Cuidado con los Bucles Infinitos", body = "Si la condición de un `while` nunca se vuelve falsa, ¡tu app se colgará! Siempre asegúrate de tener una condición de salida (como `i++`).")
    )

    // --- Sesión 9: Null Safety (6 Pasos / 3 Preguntas) ---
    private val k0_s9_steps = listOf(
        LessonStep.Concept(id = 1, title = "¡El terror de los Nulos!", body = "En lenguajes como Java, el `NullPointerException` es el error más común. Ocurre cuando intentas usar una variable que es `null`.\n\nKotlin soluciona esto desde su diseño."),
        LessonStep.Concept(id = 2, title = "Tipos No-Nulos", body = "Por defecto, ¡las variables en Kotlin no pueden ser `null`!\n\n`var nombre: String = \"Ana\"`\n`nombre = null // ERROR DE COMPILACIÓN`"),
        LessonStep.Concept(id = 3, title = "Tipos Nulos (`?`)", body = "Para permitir que una variable sea `null`, debes añadir un `?` al tipo.\n\n`var nombre: String? = \"Ana\"`\n`nombre = null // ¡Correcto!`"),
        LessonStep.FillBlank(id = 4, codeSnippet = "Declara una variable de tipo `Int` que pueda ser `null`.\n\n`var edad: `_____` = null`", options = listOf("Int", "Int?", "Int!"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 5, title = "Llamada Segura (`?.`)", body = "Para usar una variable nula de forma segura, usa el operador `?.`.\n\n`val tamano = nombre?.length`\n\nSi `nombre` es `null`, `tamano` será `null` (¡y no un error!)"),
        LessonStep.PredictResult(id = 6, codeSnippet = "var nombre: String? = null\nprint(nombre?.length)", options = listOf("0", "Error (Crash)", "null"), correctAnswerIndex = 2)
    )

    // --- Sesión 10: Proyecto Final (Calculadora de Propinas) (3 Pasos / 1 Pregunta) ---
    private val k0_s10_steps = listOf(
        LessonStep.Concept(id = 1, title = "Proyecto: Calculadora de Propinas", body = "¡Hora de combinar todo!\n\nTu reto es crear una función `calcularPropina(totalCuenta: Double, porcentaje: Int): Double`."),
        LessonStep.Concept(id = 2, title = "Pistas", body = "1. Necesitarás `val` para los parámetros.\n2. La fórmula es `(totalCuenta * porcentaje) / 100`.\n3. Asegúrate de que la función `return` un `Double`."),
        LessonStep.FillBlank(id = 3, codeSnippet = "fun calcularPropina(total: Double, porc: Int): Double {\n  `______` (total * porc) / 100.0\n}", options = listOf("print", "val", "return"), correctAnswerIndex = 2)
    )


    // --- CURSO NIVEL 1: "DESDE CERO" ---
    private val kotlin_cero = CourseDetails(
        id = "kotlin_cero",
        title = "Kotlin desde Cero: Tu primer programa",
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO,
        sessions = listOf(
            Session("k0_s1", "1. Variables y Tipos de Datos", "Aprende qué son val, var y los tipos básicos.", k0_s1_steps),
            Session("k0_s2", "2. Entorno de Desarrollo", "Configura IntelliJ IDEA, tu herramienta de trabajo.", k0_s2_steps),
            Session("k0_s3", "3. Hola Mundo y Println", "Escribe y ejecuta tu primer programa.", k0_s3_steps),
            Session("k0_s4", "4. Operadores y Lógica", "Descubre cómo hacer cálculos y comparaciones.", k0_s4_steps),
            Session("k0_s5", "5. Control de Flujo (If / When)", "Toma decisiones en tu código.", k0_s5_steps),
            Session("k0_s6", "6. Funciones Básicas", "Organiza tu código en bloques reutilizables.", k0_s6_steps),
            Session("k0_s7", "7. Colecciones (Listas)", "Aprende a guardar múltiples valores.", k0_s7_steps),
            Session("k0_s8", "8. Bucles (For / While)", "Repite tareas varias veces.", k0_s8_steps),
            Session("k0_s9", "9. Null Safety (¿Qué es ?)", "Entiende la característica más famosa de Kotlin.", k0_s9_steps),
            Session("k0_s10", "10. Proyecto: Mini-Calculadora", "Combina todo lo aprendido.", k0_s10_steps)
        )
    )

    // --- CURSOS DUMMY (PARA RELLENO) ---
    private val k0_dummy_steps = listOf(
        LessonStep.Concept(id = 1, title = "En Construcción", body = "Este curso estará disponible pronto.")
    )

    private val kotlin_cero_2 = CourseDetails(
        id = "kotlin_cero_2",
        title = "Kotlin: Siguientes Pasos",
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO,
        sessions = listOf(Session("k0_d1", "Lección 1", "Próximamente...", k0_dummy_steps))
    )
    private val kotlin_cero_3 = CourseDetails(
        id = "kotlin_cero_3",
        title = "Kotlin: Conceptos Básicos",
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO,
        sessions = listOf(Session("k0_d2", "Lección 1", "Próximamente...", k0_dummy_steps))
    )
    private val kotlin_cero_4 = CourseDetails(
        id = "kotlin_cero_4",
        title = "Kotlin: Fundamentos de Datos",
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO,
        sessions = listOf(Session("k0_d3", "Lección 1", "Próximamente...", k0_dummy_steps))
    )
    private val kotlin_cero_5 = CourseDetails(
        id = "kotlin_cero_5",
        title = "Kotlin: Mini-Proyectos",
        language = Lenguaje.KOTLIN,
        level = Nivel.DESDE_CERO,
        sessions = listOf(Session("k0_d4", "Lección 1", "Próximamente...", k0_dummy_steps))
    )

    // =================================================================================
    // --- NIVEL 2: PRINCIPIANTE (ID: kotlin_principiante) ---
    // =================================================================================

    // --- Sesión 1: Clases y Objetos (7 Pasos / 4 Preguntas) ---
    private val k1_s1_steps = listOf(
        LessonStep.Concept(id = 1, title = "Programación Orientada a Objetos (POO)", body = "La POO es un paradigma para organizar código usando 'Clases' (plantillas) y 'Objetos' (instancias de esas plantillas). Piensa en 'Clase' como el plano de un coche y 'Objeto' como el coche real que conduces."),
        LessonStep.FillBlank(id = 2, codeSnippet = "Para crear una plantilla para un Coche, usamos la palabra clave:\n\n`___ Coche`", options = listOf("fun", "val", "class"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 3, title = "Constructores y Propiedades", body = "Las clases tienen 'propiedades' (variables) que definen sus características. Se declaran en el constructor.\n\n`class Coche(val marca: String, var velocidad: Int)`"),
        LessonStep.Concept(id = 4, title = "Crear un Objeto (Instancia)", body = "Para crear un objeto real desde la plantilla (clase), simplemente llámala como si fuera una función:\n\n`val miCoche = Coche(\"Toyota\", 0)`"),
        LessonStep.FindError(id = 5, codeLines = listOf("class Persona(val nombre: String)", "val ana = Persona()"), correctLineIndex = 1),
        LessonStep.Concept(id = 6, title = "Métodos (Funciones de Clase)", body = "Las clases también pueden tener funciones, llamadas 'métodos', que definen su comportamiento.\n\n`class Coche(...) {`\n`  fun acelerar() { velocidad += 10 }`\n`}`"),
        LessonStep.PredictResult(id = 7, codeSnippet = "class Auto(var vel: Int) {\n  fun acelerar() { vel += 10 }\n}\nval miAuto = Auto(0)\nmiAuto.acelerar()\nprint(miAuto.vel)", options = listOf("0", "10", "Error"), correctAnswerIndex = 1)
    )

    // --- Sesión 2: Herencia (6 Pasos / 3 Preguntas) ---
    private val k1_s2_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es la Herencia?", body = "La herencia permite que una clase (hija) adquiera las propiedades y métodos de otra clase (padre). Se usa para reutilizar código y crear jerarquías."),
        LessonStep.Concept(id = 2, title = "Clases 'open'", body = "En Kotlin, las clases están 'final' (no se pueden heredar) por defecto. Para permitir que una clase sea heredada, debes marcarla como `open`.\n\n`open class Vehiculo`"),
        LessonStep.FillBlank(id = 3, codeSnippet = "Para que `Coche` herede de `Vehiculo`:\n\n`class Coche : Vehiculo()`", options = listOf(":", "extends", "inherits"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 4, title = "Sobrescritura (override)", body = "Una clase hija puede modificar el comportamiento de un método del padre. El método padre debe ser `open` y el método hijo debe usar `override`."),
        LessonStep.FindError(id = 5, codeLines = listOf("open class Animal {\n  fun hacerSonido() { print(\"...\") }\n}", "class Perro : Animal() {\n  override fun hacerSonido() { print(\"Guau\") }\n}"), correctLineIndex = 1), // El método padre 'hacerSonido' debe ser 'open'
        LessonStep.PredictResult(id = 6, codeSnippet = "open class A { open fun test() = \"A\" }\nclass B : A() { override fun test() = \"B\" }\nval obj: A = B()\nprint(obj.test())", options = listOf("A", "B", "Error"), correctAnswerIndex = 1)
    )

    // --- Sesión 3: Data Classes (5 Pasos / 3 Preguntas) ---
    private val k1_s3_steps = listOf(
        LessonStep.Concept(id = 1, title = "Data Classes", body = "Kotlin ofrece una forma corta de crear clases que *solo* guardan datos. Se marcan con la palabra `data`.\n\n`data class Usuario(val nombre: String, val edad: Int)`"),
        LessonStep.Concept(id = 2, title = "Funciones Automáticas", body = "Al usar `data class`, Kotlin genera automáticamente las funciones `.equals()`, `.hashCode()`, `.toString()` y `.copy()`."),
        LessonStep.PredictResult(id = 3, codeSnippet = "data class User(val name: String)\nval user1 = User(\"Ana\")\nprint(user1)", options = listOf("User(name=Ana)", "Un código hash raro", "Error"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 4, title = "Función .copy()", body = "Permite crear una copia de un objeto, cambiando solo algunas propiedades.\n\n`val user2 = user1.copy(name = \"Juan\")`"),
        LessonStep.FillBlank(id = 5, codeSnippet = "val user1 = User(\"Ana\")\nval user2 = user1.`____`()", options = listOf("clone", "copy", "duplicate"), correctAnswerIndex = 1)
    )

    // --- Sesión 4: Interfaces (6 Pasos / 3 Preguntas) ---
    private val k1_s4_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es una Interfaz?", body = "Una interfaz es un 'contrato'. Define un conjunto de métodos y propiedades que una clase *debe* implementar.\n\nUna clase puede heredar de una sola clase (`open`), pero puede implementar *múltiples* interfaces."),
        LessonStep.FillBlank(id = 2, codeSnippet = "Para definir un contrato, usamos la palabra clave:\n\n`_______ Volador`", options = listOf("class", "open", "interface"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 3, title = "Implementar una Interfaz", body = "Al igual que la herencia, se usa `:`.\n\n`interface Volador { fun volar() }`\n`class Pajaro : Volador {`\n`  override fun volar() { print(\"Volando\") }`\n`}`"),
        LessonStep.FindError(id = 4, codeLines = listOf("interface Animal { fun comer() }", "class Perro : Animal {}"), correctLineIndex = 1), // Perro no implementa comer()
        LessonStep.Concept(id = 5, title = "Propiedades en Interfaces", body = "Las interfaces también pueden declarar propiedades que las clases deben implementar (o proveer un valor por defecto)."),
        LessonStep.PredictResult(id = 6, codeSnippet = "interface Test { val x: Int }\nclass MiClase : Test {\n  override val x: Int = 20\n}\nprint(MiClase().x)", options = listOf("x", "Error", "20"), correctAnswerIndex = 2)
    )

    // --- Sesión 5: Extension Functions (6 Pasos / 3 Preguntas) ---
    private val k1_s5_steps = listOf(
        LessonStep.Concept(id = 1, title = "Funciones de Extensión", body = "Kotlin te permite 'añadir' nuevas funciones a clases que no son tuyas (¡incluso a `String` o `Int`!)."),
        LessonStep.Concept(id = 2, title = "Sintaxis", body = "Se definen fuera de la clase, prefijando el nombre de la clase al de la función.\n\n`fun String.gritar(): String {`\n`  return this.uppercase() + \"!!!\"`\n`}`"),
        LessonStep.PredictResult(id = 3, codeSnippet = "fun String.saludo(): String {\n  return \"Hola, \$this\"\n}\nprint(\"Kotlin\".saludo())", options = listOf("Hola, Kotlin", "Hola, this", "Error"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 4, title = "La palabra 'this'", body = "Dentro de una función de extensión, `this` se refiere al objeto sobre el cual se está llamando la función (en el ejemplo anterior, era \"Kotlin\")."),
        LessonStep.FillBlank(id = 5, codeSnippet = "fun Int.duplicar(): Int {\n  return `___` * 2\n}", options = listOf("it", "this", "val"), correctAnswerIndex = 1),
        LessonStep.FindError(id = 6, codeLines = listOf("fun String.cortar(n: Int) = this.substring(0, n)", "val texto = \"HolaMundo\"", "print(texto.cortar)"), correctLineIndex = 2) // Falta el parámetro: cortar(4)
    )

    // --- Sesión 6: Enum y Sealed Classes (7 Pasos / 4 Preguntas) ---
    private val k1_s6_steps = listOf(
        LessonStep.Concept(id = 1, title = "Enum Classes", body = "Las clases `enum` se usan para representar un conjunto fijo de valores constantes.\n\n`enum class Color { ROJO, VERDE, AZUL }`"),
        LessonStep.FillBlank(id = 2, codeSnippet = "Accedemos a un valor del enum usando:\n\n`val miColor = Color.`___`", options = listOf("ROJO", "\"ROJO\"", "get(0)"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 3, title = "Enums con 'when'", body = "Son perfectos para usarlos con `when`. El compilador te puede avisar si te faltó un caso."),
        LessonStep.Concept(id = 4, title = "Sealed Classes", body = "Las clases `sealed` (selladas) son la versión avanzada de los enums. Permiten que los valores (que son otras clases) tengan diferentes propiedades.\n\n`sealed class Resultado {`\n`  data class Exito(val data: String) : Resultado()`\n`  data class Error(val code: Int) : Resultado()`\n`}`"),
        LessonStep.Concept(id = 5, title = "Ventaja de Sealed Class", body = "Son perfectas para manejar estados (Cargando, Exito, Error) y obligan a que el `when` sea exhaustivo (cubra todos los casos)."),
        LessonStep.FillBlank(id = 6, codeSnippet = "Para definir un conjunto fijo de constantes, usas:\n\n`___ class Direccion { NORTE, SUR }`", options = listOf("sealed", "data", "enum"), correctAnswerIndex = 2),
        LessonStep.FindError(id = 7, codeLines = listOf("sealed class Resultado", "data class Exito(val d: String) : Resultado()", "object Cargando : Resultado()"), correctLineIndex = 0) // Falta '()' en la definición
    )

    // --- Sesión 7: Manejo de Errores (Try/Catch) (6 Pasos / 3 Preguntas) ---
    private val k1_s7_steps = listOf(
        LessonStep.Concept(id = 1, title = "Excepciones", body = "Una 'excepción' es un error que ocurre mientras el programa se ejecuta (ej. dividir por cero). Si no se maneja, la app crashea."),
        LessonStep.Concept(id = 2, title = "Try / Catch", body = "Usamos un bloque `try-catch` para 'intentar' ejecutar un código que podría fallar, y 'atrapar' el error si ocurre.\n\n`try {`\n`  // Código peligroso`\n`} catch (e: Exception) {`\n`  // Manejar el error`\n`}`"),
        LessonStep.FillBlank(id = 3, codeSnippet = "El bloque que SIEMPRE se ejecuta, falle o no, es:\n\n`try { ... } catch { ... } `_____` { ... }`", options = listOf("finally", "always", "else"), correctAnswerIndex = 0),
        LessonStep.PredictResult(id = 4, codeSnippet = "val num = 0\ntry {\n  print(10 / num)\n} catch (e: Exception) {\n  print(\"Error\")\n}\nprint(\" Fin\")", options = listOf("Error Fin", "Crash", "Fin"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 5, title = "Lanzar Excepciones (throw)", body = "Tú también puedes 'lanzar' tus propios errores.\n\n`if (edad < 0) {`\n`  throw Exception(\"Edad no puede ser negativa\")`\n`}`"),
        LessonStep.FindError(id = 6, codeLines = listOf("try {", "  val x = 10 / 0", "} (e: Exception) {", "  print(\"Error\")", "}"), correctLineIndex = 2) // Falta 'catch'
    )

    // --- CURSO NIVEL 2: "PRINCIPIANTE" ---
    private val kotlin_principiante = CourseDetails(
        id = "kotlin_principiante",
        title = "Kotlin Principiante: POO y Más",
        language = Lenguaje.KOTLIN,
        level = Nivel.PRINCIPIANTE,
        sessions = listOf(
            Session("k1_s1", "1. Clases y Objetos", "Entiende la base de la POO.", k1_s1_steps),
            Session("k1_s2", "2. Herencia y Polimorfismo", "Aprende a reutilizar código y crear jerarquías.", k1_s2_steps),
            Session("k1_s3", "3. Data Classes", "La forma simple de crear clases de datos.", k1_s3_steps),
            Session("k1_s4", "4. Interfaces", "Define contratos que tus clases deben seguir.", k1_s4_steps),
            Session("k1_s5", "5. Extension Functions", "Añade funciones a clases existentes.", k1_s5_steps),
            Session("k1_s6", "6. Enum y Sealed Classes", "Define un conjunto limitado de tipos.", k1_s6_steps),
            Session("k1_s7", "7. Manejo de Errores (Try/Catch)", "Captura y maneja excepciones.", k1_s7_steps)
        )
    )

    // =================================================================================
    // --- NIVEL 3: INTERMEDIO (ID: kotlin_intermedio) ---
    // =================================================================================

    // --- Sesión 1: Lambdas y HOFs (7 Pasos / 4 Preguntas) ---
    private val k2_s1_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es una Lambda?", body = "Una Lambda es una 'función anónima'. Es una función sin nombre que puedes pasar como parámetro o asignar a una variable.\n\nSintaxis: `{ parámetros -> cuerpo }`"),
        LessonStep.PredictResult(id = 2, codeSnippet = "val sumar = { a: Int, b: Int -> a + b }\nprint(sumar(5, 3))", options = listOf("a + b", "Error", "8"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 3, title = "Funciones de Orden Superior (HOFs)", body = "Una HOF es una función que toma otra función (una lambda) como parámetro, o devuelve una función."),
        LessonStep.Concept(id = 4, title = "Ejemplo: .forEach", body = "`.forEach` es una HOF en las listas. Recibe una lambda y la ejecuta para cada item.\n\n`listOf(1, 2).forEach { print(it) }` // Imprime 1 y luego 2"),
        LessonStep.FillBlank(id = 5, codeSnippet = "La variable por defecto en una lambda de un solo parámetro es:\n\n`lista.forEach { print(`__`) }`", options = listOf("item", "val", "it"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 6, title = "Ejemplo: .map", body = "`.map` es una HOF que transforma una lista. Recibe una lambda que dice cómo transformar cada item y devuelve una *nueva* lista.\n\n`val dobles = listOf(1, 2).map { it * 2 }`"),
        LessonStep.PredictResult(id = 7, codeSnippet = "val lista = listOf(1, 2, 3)\nval nuevaLista = lista.map { it + 1 }\nprint(nuevaLista)", options = listOf("[1, 2, 3]", "[2, 3, 4]", "Error"), correctAnswerIndex = 1)
    )

    // --- Sesión 2: Scope Functions (7 Pasos / 4 Preguntas) ---
    private val k2_s2_steps = listOf(
        LessonStep.Concept(id = 1, title = "Scope Functions", body = "Son funciones (`let`, `run`, `with`, `apply`, `also`) que ejecutan un bloque de código en un objeto. Su principal uso es hacer el código más limpio y legible."),
        LessonStep.Concept(id = 2, title = "Usando '.let'", body = "`.let` se usa comúnmente para ejecutar código solo si un valor no es nulo.\n\n`nombre?.let {`\n`  print(\"El nombre es \$it\")`\n`}`"),
        LessonStep.FillBlank(id = 3, codeSnippet = "Dentro de `let`, el objeto se llama por defecto:\n\n`nombre?.let { print(`__`) }`", options = listOf("this", "it", "self"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 4, title = "Usando '.apply'", body = "`.apply` se usa para configurar las propiedades de un objeto. Devuelve el *mismo* objeto.\n\n`val miCoche = Coche().apply {`\n`  marca = \"Ford\"`\n`  velocidad = 50`\n`}`"),
        LessonStep.FillBlank(id = 5, codeSnippet = "Dentro de `apply`, el objeto se llama:\n\n`persona.apply { `__`.edad = 20 }`", options = listOf("this", "it", "self"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 6, codeLines = listOf("var nombre: String? = \"Ana\"", "nombre.let { print(it) }"), correctLineIndex = 1), // Debe ser 'nombre?.let'
        LessonStep.PredictResult(id = 7, codeSnippet = "val lista = mutableListOf(\"a\")\nlista.apply { add(\"b\") }.add(\"c\")\nprint(lista)", options = listOf("[a, b]", "[a, b, c]", "[a, c]"), correctAnswerIndex = 1)
    )

    // --- Sesión 3: Colecciones Avanzadas (7 Pasos / 4 Preguntas) ---
    private val k2_s3_steps = listOf(
        LessonStep.Concept(id = 1, title = "Set", body = "Un `Set` (como `setOf()` o `mutableSetOf()`) es una colección que **no permite elementos duplicados**."),
        LessonStep.PredictResult(id = 2, codeSnippet = "val numeros = setOf(1, 2, 2, 3)\nprint(numeros.size)", options = listOf("4", "3", "Error"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 3, title = "Map", body = "Un `Map` (como `mapOf()` o `mutableMapOf()`) es una colección de pares **Clave-Valor**. Cada clave es única.\n\n`val edades = mapOf(\"Ana\" to 20, \"Juan\" to 30)`"),
        LessonStep.FillBlank(id = 4, codeSnippet = "Para obtener el valor de \"Ana\" en el mapa 'edades':\n\n`val edadAna = edades[`___`]`", options = listOf("\"Ana\"", "0", "Ana"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 5, title = "HOFs: .filter", body = "`.filter` es una HOF que crea una nueva lista solo con los elementos que cumplen una condición.\n\n`val pares = listOf(1, 2, 3, 4).filter { it % 2 == 0 }`"),
        LessonStep.PredictResult(id = 6, codeSnippet = "val nombres = listOf(\"Ana\", \"Luis\", \"Sara\")\nval conA = nombres.filter { it.startsWith(\"A\") }\nprint(conA)", options = listOf("[Ana]", "[Ana, Sara]", "Error"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 7, codeLines = listOf("val mapa = mapOf(\"a\" to 1)", "mapa[\"a\"] = 2"), correctLineIndex = 1) // Es inmutable
    )

    // --- Sesión 4: Genéricos (6 Pasos / 3 Preguntas) ---
    private val k2_s4_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué son los Genéricos?", body = "Los genéricos permiten crear clases o funciones que funcionan con *cualquier tipo* de dato. Se usa `<T>` como un marcador de posición para el tipo."),
        LessonStep.Concept(id = 2, title = "Clases Genéricas", body = "Seguramente ya las usaste: `List<T>` es una clase genérica. `List<Int>` es una lista de enteros, `List<String>` es una lista de Strings.\n\n`class Caja<T>(val item: T)`"),
        LessonStep.FillBlank(id = 3, codeSnippet = "class Caja<T>(val item: T)\nval cajaDeTexto = Caja<`___`>(\"Hola\")", options = listOf("T", "String", "Any"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 4, title = "Funciones Genéricas", body = "Las funciones también pueden ser genéricas.\n\n`fun <T> imprimirItem(item: T) {`\n`  print(item)`\n`}`"),
        LessonStep.PredictResult(id = 5, codeSnippet = "class Caja<T>(val item: T)\nval caja = Caja(10)\nprint(caja.item + 5)", options = listOf("15", "10", "Error"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 6, codeLines = listOf("class Caja<T>(val item: T)", "val caja: Caja<Int> = Caja(\"Hola\")"), correctLineIndex = 1)
    )

    // --- Sesión 5: Intro a Coroutines (6 Pasos / 3 Preguntas) ---
    private val k2_s5_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es una Coroutine?", body = "Una Coroutine es un 'hilo ligero'. Permite ejecutar tareas largas (API, DB) *sin* bloquear el hilo principal de la app."),
        LessonStep.Concept(id = 2, title = "Coroutine Scope", body = "Toda coroutine debe iniciarse en un 'Scope' (alcance), que controla su ciclo de vida. En Android, usamos `viewModelScope`."),
        LessonStep.Concept(id = 3, title = "`launch`: Dispara y Olvida", body = "Usamos `launch` para iniciar una coroutine que no devuelve un resultado. Es ideal para actualizar la UI o guardar en la base de datos.\n\n`viewModelScope.launch { ... }`"),
        LessonStep.Concept(id = 4, title = "`suspend fun` (Funciones Suspendidas)", body = "La palabra clave `suspend` marca una función que puede ser 'pausada' y 'reanudada'.\n\n`delay(1000)` es un ejemplo: pausa la coroutine por 1 segundo sin bloquear el hilo."),
        LessonStep.FillBlank(id = 5, codeSnippet = "Para pausar una coroutine por 2 segundos, usamos:\n\n`_______(2000)`", options = listOf("Thread.sleep", "wait", "delay"), correctAnswerIndex = 2),
        LessonStep.FindError(id = 6, codeLines = listOf("fun miFuncion() {", "  delay(1000)", "  print(\"Hola\")", "}"), correctLineIndex = 1)
    )

    // --- Sesión 6: Coroutine Scopes & Jobs (5 Pasos / 3 Preguntas) ---
    private val k2_s6_steps = listOf(
        LessonStep.Concept(id = 1, title = "El 'Job'", body = "Cada coroutine iniciada con `launch` devuelve un `Job`. Este objeto te permite controlar la coroutine (cancelarla, esperar a que termine, etc.)."),
        LessonStep.FillBlank(id = 2, codeSnippet = "val miJob = viewModelScope.launch { ... }\n\nmiJob.`_____`() // Para detener la coroutine", options = listOf("stop", "kill", "cancel"), correctAnswerIndex = 2),
        LessonStep.Concept(id = 3, title = "CoroutineScope", body = "Un `CoroutineScope` (como `viewModelScope`) define el 'contexto' y el ciclo de vida. Si el `viewModelScope` se cancela (porque el ViewModel se destruye), todos los `Job` hijos se cancelan automáticamente."),
        LessonStep.FindError(id = 4, codeLines = listOf("val job1 = launch { delay(1000) }", "val job2 = launch { delay(2000) }", "job1.join()", "job2.join()"), correctLineIndex = -1),
        LessonStep.PredictResult(id = 5, codeSnippet = "val job = launch { delay(100) }\njob.cancel()\n// ¿La coroutine se ejecuta?", options = listOf("Sí", "No", "A veces"), correctAnswerIndex = 1)
    )

    // --- Sesión 7: Dispatchers (6 Pasos / 3 Preguntas) ---
    private val k2_s7_steps = listOf(
        LessonStep.Concept(id = 1, title = "Dispatchers", body = "Los Dispatchers le dicen a las coroutines en qué hilo (thread) ejecutarse.\n\n" +
                "• `Dispatchers.Main`: Hilo principal (UI en Android). Único que puede tocar la UI.\n" +
                "• `Dispatchers.IO`: Optimizado para operaciones de Red o Disco (Input/Output).\n" +
                "• `Dispatchers.Default`: Optimizado para cálculos pesados (CPU)."),
        LessonStep.FillBlank(id = 2, codeSnippet = "Para hacer una llamada a una API (Red), deberías usar:\n\n`withContext(Dispatchers.`___`) { ... }`", options = listOf("Main", "IO", "Default"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 3, title = "`withContext`", body = "Es la forma segura de *cambiar* de hilo dentro de una coroutine. Se usa para mover una tarea pesada (IO/Default) fuera del hilo Main, y luego volver."),
        LessonStep.FindError(id = 4, codeLines = listOf("viewModelScope.launch(Dispatchers.IO) {", "  val datos = api.getDatos()", "  // Error aquí:", "  miTextView.text = datos", "}"), correctLineIndex = 3),
        LessonStep.Concept(id = 5, title = "Solución al Error", body = "La forma correcta es:\n\n`viewModelScope.launch {`\n`  val datos = withContext(Dispatchers.IO) { api.getDatos() }`\n`  miTextView.text = datos // ¡Correcto! Estamos en Main`\n`}`"),
        LessonStep.FillBlank(id = 6, codeSnippet = "Para actualizar un `TextView` o un estado de Compose, debes estar en `Dispatchers.`___", options = listOf("Main", "IO", "Default"), correctAnswerIndex = 0)
    )

    // --- CURSO NIVEL 3: "INTERMEDIO" ---
    private val kotlin_intermedio = CourseDetails(
        id = "kotlin_intermedio",
        title = "Kotlin Intermedio: Programación Funcional y Asíncrona",
        language = Lenguaje.KOTLIN,
        level = Nivel.INTERMEDIO,
        sessions = listOf(
            Session("k2_s1", "1. Lambdas y HOFs", "El pilar de la programación funcional.", k2_s1_steps),
            Session("k2_s2", "2. Scope Functions (let, run...)", "Simplifica tu código con funciones de alcance.", k2_s2_steps),
            Session("k2_s3", "3. Colecciones Avanzadas (Filter, Map)", "Domina la manipulación de listas.", k2_s3_steps),
            Session("k2_s4", "4. Genéricos", "Crea clases y funciones que funcionan con cualquier tipo.", k2_s4_steps),
            Session("k2_s5", "5. Introducción a Coroutines", "El inicio de la programación asíncrona.", k2_s5_steps),
            Session("k2_s6", "6. Coroutine Scopes & Jobs", "Controla el ciclo de vida de tus coroutines.", k2_s6_steps),
            Session("k2_s7", "7. Dispatchers (Main, IO, Default)", "Controla en qué hilo se ejecuta tu código.", k2_s7_steps)
        )
    )

    // =================================================================================
    // --- NIVEL 4: AVANZADO (ID: kotlin_avanzado) ---
    // =================================================================================

    // --- Sesión 1: launch vs. async (6 Pasos / 3 Preguntas) ---
    private val k3_s1_steps = listOf(
        LessonStep.Concept(id = 1, title = "Recordatorio: `launch`", body = "`launch` 'dispara y olvida'. Inicia una coroutine y no espera un resultado. Devuelve un objeto `Job`."),
        LessonStep.Concept(id = 2, title = "`async`", body = "`async` inicia una coroutine y te permite obtener un resultado futuro. Devuelve un objeto `Deferred`."),
        LessonStep.Concept(id = 3, title = "`.await()`", body = "Usas `.await()` en un objeto `Deferred` para pausar la coroutine hasta que el resultado esté listo.\n\n`val resultado = viewModelScope.async {`\n`  api.getDatos()`\n`}`\n`val datos = resultado.await()`"),
        LessonStep.FillBlank(id = 4, codeSnippet = "Quiero ejecutar 2 llamadas de API en paralelo y luego sumar sus resultados. Debería usar:", options = listOf("Dos `launch`", "Dos `async`", "Dos `withContext`"), correctAnswerIndex = 1),
        LessonStep.PredictResult(id = 5, codeSnippet = "GlobalScope.launch {\n  print(\"Hola\")\n}\nprint(\"Mundo\")", options = listOf("HolaMundo", "MundoHola", "Error"), correctAnswerIndex = 1),
        LessonStep.FindError(id = 6, codeLines = listOf("val resultado = viewModelScope.launch {", "  delay(1000)", "  return@launch 5", "}", "val num = resultado.await()"), correctLineIndex = 4)
    )

    // --- Sesión 2: Introducción a Flow (7 Pasos / 4 Preguntas) ---
    private val k3_s2_steps = listOf(
        LessonStep.Concept(id = 1, title = "¿Qué es Flow?", body = "Un `Flow` (flujo) es como un 'Stream' de datos. Es una coroutine que puede `emitir` *múltiples* valores a lo largo del tiempo, en lugar de devolver uno solo."),
        LessonStep.Concept(id = 2, title = "Productor (flow)", body = "Se crea usando un constructor `flow { ... }`.\n\n`fun miFlujo(): Flow<Int> = flow {`\n`  emit(1)`\n`  delay(1000)`\n`  emit(2)`\n`}`"),
        LessonStep.Concept(id = 3, title = "Consumidor (collect)", body = "Para recibir los valores, usamos `.collect()`.\n\n`miFlujo().collect { valor ->`\n`  print(valor)`\n`}`\n\nEsto imprimirá '1', esperará un segundo, y luego imprimirá '2'."),
        LessonStep.FillBlank(id = 4, codeSnippet = "Para enviar un valor desde un `flow`, usamos la palabra clave:", options = listOf("return", "send", "emit"), correctAnswerIndex = 2),
        LessonStep.FillBlank(id = 5, codeSnippet = "Para recibir valores de un `flow`, usamos la función:", options = listOf("collect", "receive", "get"), correctAnswerIndex = 0),
        LessonStep.PredictResult(id = 6, codeSnippet = "flow { emit(10); emit(20) }.collect { print(it * 2) }", options = listOf("20", "40", "2040"), correctAnswerIndex = 2),
        LessonStep.FindError(id = 7, codeLines = listOf("fun miFlujo() = flow {", "  emit(1)", "}", "miFlujo() // No hace nada"), correctLineIndex = 3)
    )

    // --- Sesión 3: Operadores de Flow (6 Pasos / 3 Preguntas) ---
    private val k3_s3_steps = listOf(
        LessonStep.Concept(id = 1, title = "Operadores de Flow", body = "Al igual que las listas, los Flows tienen operadores como `.map` y `.filter` para transformar el stream de datos."),
        LessonStep.PredictResult(id = 2, codeSnippet = "flow { emit(1); emit(2) }\n  .map { it * 10 }\n  .collect { print(it) }", options = listOf("1020", "12", "Error"), correctAnswerIndex = 0),
        LessonStep.Concept(id = 3, title = "Operadores Terminales", body = "`.collect()` es el operador terminal más común, pero hay otros como `.toList()`, `.first()`, y `.reduce()`."),
        LessonStep.Concept(id = 4, title = "Flow On (Cambiando Contexto)", body = "Para cambiar el *hilo* donde se ejecuta el productor del flow (ej. una base de datos), se usa `.flowOn(Dispatchers.IO)`."),
        LessonStep.FillBlank(id = 5, codeSnippet = "Para filtrar solo los números pares de un flujo:\n\n`miFlujo.filter { it % 2 == `__` }`", options = listOf("0", "1", "null"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 6, codeLines = listOf("flow {", "  emit(1)", "}.flowOn(Dispatchers.Main)", "  .collect { ... }"), correctLineIndex = 2)
    )

    // --- Sesión 4: StateFlow y SharedFlow (7 Pasos / 4 Preguntas) ---
    private val k3_s4_steps = listOf(
        LessonStep.Concept(id = 1, title = "Flows 'Fríos' vs 'Calientes'", body = "Un Flow 'Frío' (el normal) solo empieza a emitir cuando alguien hace `.collect()`. Cada colector tiene su *propio* flujo.\n\nUn Flow 'Caliente' emite valores exista o no un colector."),
        LessonStep.Concept(id = 2, title = "StateFlow", body = "Es un Flow 'Caliente' diseñado para guardar un *estado*. Siempre tiene un valor inicial y solo emite el valor más reciente a los nuevos colectores. Es el reemplazo moderno de `LiveData`."),
        LessonStep.FillBlank(id = 3, codeSnippet = "El StateFlow de un ViewModel se expone como `StateFlow` y por dentro es un:\n\n`Mutable`_____`Flow`", options = listOf("State", "Shared", "Hot"), correctAnswerIndex = 0),
        LessonStep.PredictResult(id = 4, codeSnippet = "val state = MutableStateFlow(10)\nstate.value = 20\nstate.collect { print(it) }", options = listOf("10", "20", "1020"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 5, title = "SharedFlow", body = "Es un Flow 'Caliente' diseñado para 'Eventos'. No tiene un estado inicial, y emite valores a todos sus colectores. Ideal para eventos de 'Mostrar Toast' o 'Navegar'."),
        LessonStep.FillBlank(id = 6, codeSnippet = "Para un estado que debe ser observado (como `uiState`), usamos:", options = listOf("StateFlow", "SharedFlow", "Flow"), correctAnswerIndex = 0),
        LessonStep.FillBlank(id = 7, codeSnippet = "Para enviar un evento (como un Toast) que solo debe mostrarse una vez, usamos:", options = listOf("StateFlow", "SharedFlow", "Flow"), correctAnswerIndex = 1)
    )

    // --- Sesión 5: Serialización (kotlinx.serialization) (6 Pasos / 3 Preguntas) ---
    private val k3_s5_steps = listOf(
        LessonStep.Concept(id = 1, title = "Serialización", body = "Es el proceso de convertir un objeto de Kotlin (como una `data class`) en un formato de texto (como JSON), y viceversa."),
        LessonStep.Concept(id = 2, title = "kotlinx.serialization", body = "Es la librería oficial de Kotlin. Se usa anotando las clases con `@Serializable`."),
        LessonStep.FillBlank(id = 3, codeSnippet = "`@`_________\ndata class User(val name: String)", options = listOf("Json", "Serializable", "Convert"), correctAnswerIndex = 1),
        LessonStep.Concept(id = 4, title = "Codificar y Decodificar", body = "`Json.encodeToString(objeto)` convierte el objeto a JSON.\n\n`Json.decodeFromString<Clase>(jsonString)` convierte el JSON de vuelta al objeto."),
        LessonStep.PredictResult(id = 5, codeSnippet = "@Serializable data class U(val n: String)\nval u = U(\"Ana\")\nprint(Json.encodeToString(u))", options = listOf("{\"n\":\"Ana\"}", "{n:Ana}", "Error"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 6, codeLines = listOf("data class User(val name: String)", "val u = User(\"Ana\")", "print(Json.encodeToString(u))"), correctLineIndex = 0)
    )

    // --- Sesión 6: Introducción a Ktor (Backend) (6 Pasos / 3 Preguntas) ---
    private val k3_s6_steps = listOf(
        LessonStep.Concept(id = 1, title = "Ktor: Kotlin en el Backend", body = "Ktor es un framework de Kotlin para construir servidores y APIs. Es ligero, asíncrono (basado en coroutines) y fácil de usar."),
        LessonStep.Concept(id = 2, title = "Crear un Servidor", body = "Se crea un `embeddedServer` y se define el `routing`.\n\n`embeddedServer(Netty, port = 8080) {`\n`  routing { ... }`\n`}.start(wait = true)`"),
        LessonStep.Concept(id = 3, title = "Routing (Rutas)", body = "Defines las 'rutas' (endpoints) que tu API escuchará.\n\n`routing {`\n`  get(\"/hola\") {`\n`    call.respondText(\"Hola Mundo\")`\n`  }`\n`}`"),
        LessonStep.FillBlank(id = 4, codeSnippet = "Para responder con texto plano a una llamada, usamos:\n\n`call.respond`_____`(\"Hola\")`", options = listOf("Json", "Text", "String"), correctAnswerIndex = 1),
        LessonStep.PredictResult(id = 5, codeSnippet = "// Si un usuario visita GET /saludo\nrouting {\n  get(\"/saludo\") { call.respondText(\"OK\") }\n}", options = listOf("Responde OK", "Error 404", "Crash"), correctAnswerIndex = 0),
        LessonStep.FindError(id = 6, codeLines = listOf("routing {", "  post(\"/usuario\") { ... }", "  get(\"/usuario\") { ... }", "}"), correctLineIndex = -1)
    )

    // --- Sesión 7: Testing (JUnit y MockK) (6 Pasos / 3 Preguntas) ---
    private val k3_s7_steps = listOf(
        LessonStep.Concept(id = 1, title = "Testing con JUnit", body = "JUnit es el estándar para testing en la JVM. Usamos `@Test` para marcar una función como una prueba unitaria."),
        // --- ¡¡ERROR CORREGIDO!! ---
        LessonStep.FillBlank(
            id = 2,
            codeSnippet = "fun `test suma`() {\n  val res = 2 + 2\n  `assertEquals`(4, res)\n}",
            options = listOf("assertEquals", "assert", "check"),
            correctAnswerIndex = 0
        ),
        // --- FIN DE CORRECCIÓN ---
        LessonStep.Concept(id = 3, title = "Mocking con MockK", body = "El 'Mocking' es simular el comportamiento de una dependencia (como una API o base de datos) para aislar lo que estás probando."),
        LessonStep.Concept(id = 4, title = "Sintaxis de MockK", body = "`val miRepo = mockk<MiRepositorio>()`\n\n`every { miRepo.getDatos() } returns \"Datos Falsos\"`"),
        LessonStep.FillBlank(id = 5, codeSnippet = "Para definir qué debe devolver un mock, usamos:\n\n`_____ { miRepo.funcion() } returns \"Valor\"`", options = listOf("mock", "when", "every"), correctAnswerIndex = 2),
        LessonStep.FindError(id = 6, codeLines = listOf("fun suma(a: Int, b: Int) = a + b", "@Test fun testSuma() {", "  assertEquals(5, suma(2, 2))", "}"), correctLineIndex = 2)
    )

    // --- CURSO NIVEL 4: "AVANZADO" ---
    private val kotlin_avanzado = CourseDetails(
        id = "kotlin_avanzado",
        title = "Kotlin Avanzado: Asincronía y Backend",
        language = Lenguaje.KOTLIN,
        level = Nivel.AVANZADO,
        sessions = listOf(
            Session("k3_s1", "1. launch vs. async", "Ejecución concurrente y paralela.", k3_s1_steps),
            Session("k3_s2", "2. Introducción a Flow", "Maneja streams de datos asíncronos.", k3_s2_steps),
            Session("kD_s3", "3. Operadores de Flow (Map, Filter)", "Transforma tus flujos de datos.", k3_s3_steps),
            Session("k3_s4", "4. StateFlow y SharedFlow", "Manejo de estado y eventos en vivo.", k3_s4_steps),
            Session("k3_s5", "5. Serialización (kotlinx.serialization)", "Convierte objetos a JSON y viceversa.", k3_s5_steps),
            Session("k3_s6", "6. Introducción a Ktor (Backend)", "Crea tu propia API con Kotlin.", k3_s6_steps),
            Session("k3_s7", "7. Testing (JUnit y MockK)", "Aprende a probar tu código.", k3_s7_steps)
        )
    )

    // --- MAPA DE TODOS LOS CURSOS DE KOTLIN ---
    // --- ¡¡MAPA ACTUALIZADO CON LOS 6 CURSOS DE NIVEL CERO!! ---
    private val allKotlinCourses = mapOf(
        "kotlin_intro" to kotlin_intro, // <-- CURSO 0 AÑADIDO
        "kotlin_cero" to kotlin_cero,
        "kotlin_cero_2" to kotlin_cero_2,
        "kotlin_cero_3" to kotlin_cero_3,
        "kotlin_cero_4" to kotlin_cero_4,
        "kotlin_cero_5" to kotlin_cero_5,
        "kotlin_principiante" to kotlin_principiante,
        "kotlin_intermedio" to kotlin_intermedio,
        "kotlin_avanzado" to kotlin_avanzado
    )

    /**
     * Función pública para obtener un curso de Kotlin por su ID
     */
    fun getCourseDetails(courseId: String): CourseDetails? {
        return allKotlinCourses[courseId.lowercase()]
    }

    /**
     * Función pública para obtener los detalles de una sesión específica
     */
    fun getSessionDetails(courseId: String, sessionId: String): Session? {
        val course = getCourseDetails(courseId)
        return course?.sessions?.find { it.id == sessionId }
    }

    /**
     * Obtiene una lista de cursos de Kotlin que coincidan con el nivel.
     */
    fun getCoursesByLevel(level: Nivel): List<CourseDetails> {
        // Ordena para que "kotlin_intro" siempre aparezca primero si está en la lista
        return allKotlinCourses.values
            .filter { it.level == level }
            .sortedBy { it.id == "kotlin_intro" == false } // Truco para poner "kotlin_intro" de primero
    }
}