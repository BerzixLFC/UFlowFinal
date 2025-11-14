package me.uflow.unab.edu.uflow.data.model

import me.uflow.unab.edu.uflow.util.Lenguaje
import me.uflow.unab.edu.uflow.util.Nivel

data class UserData(
    val uid: String,
    val email: String?,
    val nombre: String = "",
    val apellido: String = "",
    val usuario: String = "",
    val cumpleanos: String = "",
    val localizacion: String = "",
    val selectedLanguage: Lenguaje? = null,
    val selectedNivel: Nivel? = null,
    val friends: List<String> = emptyList(),
    val friendRequestsSent: List<String> = emptyList(),
    val friendRequestsReceived: List<String> = emptyList(),
    val courseProgress: Map<String, String> = emptyMap(),
    val completedCourses: List<String> = emptyList(),
    val instagramHandle: String = "",
    val twitterHandle: String = "",
    val githubHandle: String = "",
    val intereses: List<String> = emptyList(),
    val bestStreak: Int = 0,
    val bestStreakLanguage: Lenguaje? = null,
    val bestStreakDifficulty: Nivel? = null,
    val attemptsCount: Int = 0,
    val lastStreak: Int = 0,
    val lastStreakLanguage: Lenguaje? = null,
    val lastStreakDifficulty: Nivel? = null,
    val courses: List<String> = emptyList(),
    val friends_DEPRECATED: List<String> = emptyList(),
    val tasks: List<String> = emptyList(),
    val habits: List<String> = emptyList()
) {
    //CONSTRUCTOR DE FIREBASE
    constructor() : this(
        uid = "",
        email = null,
        nombre = "",
        apellido = "",
        usuario = "",
        cumpleanos = "",
        localizacion = "",
        selectedLanguage = null,
        selectedNivel = null,
        friends = emptyList(),
        friendRequestsSent = emptyList(),
        friendRequestsReceived = emptyList(),
        courseProgress = emptyMap(),
        completedCourses = emptyList(),
        instagramHandle = "",
        twitterHandle = "",
        githubHandle = "",
        intereses = emptyList(),
        bestStreak = 0,
        bestStreakLanguage = null,
        bestStreakDifficulty = null,
        attemptsCount = 0,
        lastStreak = 0,
        lastStreakLanguage = null,
        lastStreakDifficulty = null,
        courses = emptyList(),
        friends_DEPRECATED = emptyList(),
        tasks = emptyList(),
        habits = emptyList()
    )
    fun getFullName(): String {
        return "$nombre $apellido".trim()
    }
}