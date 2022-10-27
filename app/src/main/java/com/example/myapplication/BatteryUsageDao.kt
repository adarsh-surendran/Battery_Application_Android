package com.example.myapplication

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface BatteryUsageDao {
    @Query("SELECT * FROM BatteryUsage")
    fun getAll(): List<BatteryUsage>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(battery: BatteryUsage)
}