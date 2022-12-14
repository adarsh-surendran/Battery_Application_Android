package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.Room
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BatteryService: Service(){
    private lateinit var db: BatteryDB
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {

        db = Room.databaseBuilder(
            applicationContext,
            BatteryDB::class.java, "BatteryDB"
        ).allowMainThreadQueries().build()
        super.onCreate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }
    companion object {
        private const val NOTIF_ID = 1
        private const val NOTIF_CHANNEL_ID = "Channel_Id"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground()
    {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        val chan = NotificationChannel(
            NOTIF_CHANNEL_ID, "My ForeGroundService", NotificationManager.IMPORTANCE_LOW
        )
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                NOTIF_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

                val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batper: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)



                val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val batteryStatus = context!!.registerReceiver(null, ifilter)
                val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                        || status == BatteryManager.BATTERY_STATUS_FULL
                val plugStatus: Int
                if (isCharging)
                    plugStatus = 1
                else
                    plugStatus = 0
                val now = LocalDateTime.now()
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                val formatDateTime = now.format(formatter)

                val battery = BatteryUsage( 0,batper.toFloat(), plugStatus,formatDateTime )
                db.batteryUsageDao().insertAll(battery)

            }
        }
    }
}

