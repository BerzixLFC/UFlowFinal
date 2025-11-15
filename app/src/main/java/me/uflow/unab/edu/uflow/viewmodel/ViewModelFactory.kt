package me.uflow.unab.edu.uflow.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.uflow.unab.edu.uflow.ui.Screen.PomodoroViewModel

class PomodoroViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            return PomodoroViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}