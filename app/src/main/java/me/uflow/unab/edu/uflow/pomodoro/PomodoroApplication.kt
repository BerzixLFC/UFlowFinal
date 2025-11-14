package me.uflow.unab.edu.uflow.ui.Screen

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class PomodoroApplication : Application() {

    companion object {
        const val CHANNEL_ID = "pomodoro_channel"
        const val CHANNEL_NAME = "Pomodoro Timer"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones del temporizador Pomodoro"
                enableVibration(true)
                setSound(
                    android.provider.Settings.System.DEFAULT_NOTIFICATION_URI,
                    null
                )
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}