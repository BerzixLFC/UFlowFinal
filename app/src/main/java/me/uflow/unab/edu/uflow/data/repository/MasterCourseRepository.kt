package me.uflow.unab.edu.uflow.data.repository

import me.uflow.unab.edu.uflow.data.model.CourseDetails
import me.uflow.unab.edu.uflow.data.model.Session
import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

//REPOSITORY MAESTRO QUE DELEGA LA BUSQUEDA DE CURSOS
object MasterCourseRepository {

    //OBTIENE LOS DETALLES DE UN CURSO POR SU ID
    fun getCourseDetails(courseId: String): CourseDetails? {
        val id = courseId.lowercase()

        // Kotlin
        var course = KotlinCourseRepository.getCourseDetails(id)
        if (course != null) return course
        
        return KotlinCourseRepository.getCourseDetails("kotlin_cero")
    }
    
    //OBTIENE LOS DETALLES DE UNA SESION ESPECIFICA
    fun getSessionDetails(courseId: String, sessionId: String): Session? {
        val id = courseId.lowercase()
        
        var session = KotlinCourseRepository.getSessionDetails(id, sessionId)
        if (session != null) return session

        return null
    }

    //Obtiene UNA LISTA DE TODOS LOS CURSOS ESPECIFICOS POR LENGUAJE Y NIVEL
    fun getCoursesByLanguageAndLevel(language: Lenguaje, level: Nivel): List<CourseDetails> {
        return when (language) {
            Lenguaje.KOTLIN -> KotlinCourseRepository.getCoursesByLevel(level)
            Lenguaje.JAVA -> emptyList() // TODO: 
            Lenguaje.PYTHON -> emptyList() // TODO: 
            Lenguaje.WEB -> emptyList() // TODO: 
        }
    }
}