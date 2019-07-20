package com.justaclock.receivers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.justaclock.R

class AlarmNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context)

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm actived! 7u7")
                .setContentText("THIS IS MY ALARM 7u7r")
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                .setContentInfo("Info nope")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}