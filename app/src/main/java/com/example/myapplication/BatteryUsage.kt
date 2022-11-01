package com.example.myapplication
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class BatteryUsage(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "Battery_Percent") val batteryPercent: Float,
    @ColumnInfo(name = "Battery_Status") val batteryStatus: Int,
    @ColumnInfo(name = "Timestamp") val timestamp: String,

)
