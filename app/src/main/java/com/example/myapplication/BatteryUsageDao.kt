package com.example.myapplication

import androidx.room.*

@Dao
interface BatteryUsageDao {
    @Query("SELECT * FROM BatteryUsage")
    fun getAll(): List<BatteryUsage>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg battery: BatteryUsage)
    @Query("SELECT * FROM BatteryUsage WHERE Timestamp BETWEEN  :startTime AND :endTime")
    fun getHourly( startTime:String, endTime:String):List<BatteryUsage>

}