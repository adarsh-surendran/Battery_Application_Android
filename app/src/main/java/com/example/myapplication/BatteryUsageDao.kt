package com.example.myapplication

import androidx.room.*

@Dao
interface BatteryUsageDao {
    @Query("SELECT * FROM BatteryUsage")
    fun getAll(): List<BatteryUsage>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg battery: BatteryUsage)
    @Query("SELECT * FROM BatteryUsage where Timestamp between  :startTime AND :endTime")
    fun getHourly(startTime:String,endTime:String):List<BatteryUsage>
//    @Query("Delete From BatteryUsage")
//    fun deleteAll()
//    @Query("UPDATE BatteryUsage set id=0")
//    fun reset()

}