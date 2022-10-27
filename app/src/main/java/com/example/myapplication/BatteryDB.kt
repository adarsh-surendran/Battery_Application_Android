package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BatteryUsage :: class], version = 1)
abstract class BatteryDB :RoomDatabase(){
    abstract fun batteryUsageDao() : BatteryUsageDao

    companion object{

        @Volatile
        private var INSTANCE : BatteryDB? = null

        fun getDatabase(context: Context): BatteryDB{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BatteryDB::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }
}