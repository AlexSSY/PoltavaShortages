package com.alex.ps.infrastructure

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alex.ps.R

class Notifier(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "poe_status_channel"
        private const val CHANNEL_NAME = "POE notifications"
    }

    init {
        createNotificationChannel()
    }

    fun notifyGav(isGavEnabled: Boolean) {
        val title = "ГАВ"
        val message =
            if (isGavEnabled) "В области введен ГАВ"
            else "В области отменен ГАВ"

        showNotification(
            id = 1,
            title = title,
            message = message
        )
    }

    fun notifyTodayScheduleChanged() {
        showNotification(
            id = 2,
            title = "График на сегодня",
            message = "График на сегодня изменился"
        )
    }

    fun notifyTomorrowScheduleAvailable() {
        showNotification(
            id = 3,
            title = "График на завтра",
            message = "Появился график на завтра"
        )
    }

    private fun showNotification(
        id: Int,
        title: String,
        message: String
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(id, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun notifyTenMinutesBeforePowerOff() {
        showNotification(
            id = 4,
            title = "Отключение света",
            message = "Выключат свет через 10 минут!!!"
        )
    }

    fun notifySpecGav(isGavEnabled: Boolean) {
        val title = "Спец. ГАВ"
        val message =
            if (isGavEnabled) "В области введен СГАВ"
            else "В области отменен СГАВ"

        showNotification(
            id = 5,
            title = title,
            message = message
        )
    }

    fun notifyTomorrowScheduleChanged() {
        val title = "График на завтра"
        val message = "График на завтра изменился"

        showNotification(
            id = 6,
            title = title,
            message = message
        )
    }
}