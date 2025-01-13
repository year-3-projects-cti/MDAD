package com.example.taskit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationForegroundService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval: Long = 30000 // Check every 30 seconds
    private var isRunning = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val CHANNEL_ID = "LocationServiceChannel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startForegroundService()
            }
            ACTION_STOP -> {
                stopForegroundService()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundService() {
        if (isRunning) return
        isRunning = true

        val notification = createNotification("Monitoring location status...")
        startForeground(NOTIFICATION_ID, notification)

        handler.post(object : Runnable {
            override fun run() {
                checkProximityToPolitehnica()
                handler.postDelayed(this, checkInterval)
            }
        })
    }

    private fun stopForegroundService() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
        stopSelf()
    }

    private fun checkProximityToPolitehnica() {
        val politehnicaLat = 44.438753
        val politehnicaLng = 26.049482
        val radiusInMeters = 500 // 500 meters

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val distance = FloatArray(1)
                Location.distanceBetween(
                    it.latitude, it.longitude,
                    politehnicaLat, politehnicaLng,
                    distance
                )
                if (distance[0] <= radiusInMeters) {
                    sendPolitehnicaNotification()
                }
            }
        }
    }

    private fun sendPolitehnicaNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Welcome to Politehnica!")
            .setContentText("Enjoy your visit to the faculty!")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotification(message: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Location Service Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }
}
