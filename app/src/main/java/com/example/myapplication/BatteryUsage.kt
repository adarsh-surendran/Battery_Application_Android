package com.example.myapplication
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BatteryUsage(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "Battery_Percent") val batteryPercent: Float?,
    @ColumnInfo(name = "Battery_Status") val batteryStatus: String?,
    @ColumnInfo(name = "Timestamp") val timestamp: String?,

)
