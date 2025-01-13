package com.example.taskit

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.taskit.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationDebug", "NotificationReceiver triggered")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("NotificationDebug", "Permission not granted")
            return
        }

        val title = intent.getStringExtra("title") ?: "Class Reminder"
        val room = intent.getStringExtra("room") ?: "Unknown Room"

        Log.d("NotificationDebug", "Received title: $title, room: $room")

        val notification = NotificationCompat.Builder(context, "class_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Upcoming Class: $title")
            .setContentText("Your class in $room starts in 2 hours.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
