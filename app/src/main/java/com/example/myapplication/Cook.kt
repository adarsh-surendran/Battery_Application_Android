package com.example.myapplication

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Cook {
    private lateinit var db:BatteryDB
    private var listData:MutableList<DischargeDetails>  = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    fun dischargeCal(context: Context):MutableList<DischargeDetails>{
        listData.clear()
        db = Room.databaseBuilder(
            context,
            BatteryDB::class.java, "BatteryDB"
        ).allowMainThreadQueries().build()
        val datas:List<BatteryUsage> =db.batteryUsageDao().getAll()
        var startDate=datas[0].timestamp
        var tempstartDate=startDate
        var charge=datas[0].batteryPercent
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        while (true){
            val dateTime = LocalDateTime.parse(startDate,formatter)
            var strday=dateTime.dayOfMonth.toString()
            var strmonth=dateTime.monthValue.toString()
            var strhour=dateTime.hour.toString()
            if(strday.length==1){
                strday= "0$strday"
            }
            if(strmonth.length==1){
                strmonth= "0$strmonth"
            }
            if(strhour.length==1){
                strhour= "0$strhour"
            }
            var strhour2=(strhour.toInt()+1).toString()
            if(strhour2.length==1){
                strhour2= "0$strhour2"
            }

            startDate=""+dateTime.year+"-"+strmonth+"-"+strday+" "+strhour+":00:00"
            var endDate=""+dateTime.year+"-"+strmonth+"-"+strday+" "+strhour2+":00:00"
            val hourlyData:List<BatteryUsage> =db.batteryUsageDao().getHourly(startDate,endDate)
            if (hourlyData.count()>0){
                var initialCharge=charge
                var initialTime=LocalDateTime.parse(tempstartDate,formatter)
                var dischargeLevel=0.0
                var duration:Duration
                var dischargeTime:Long=0
                var i=1
                while (i<hourlyData.count()){
                    var finalCharge=hourlyData[i].batteryPercent
                    var finalTime=LocalDateTime.parse(hourlyData[i].timestamp,formatter)
                    var status=hourlyData[i].batteryStatus
                    if (status==0 && initialCharge!=finalCharge){
                        dischargeLevel+=initialCharge-finalCharge
                        duration= Duration.between(initialTime,finalTime)
                        dischargeTime=duration.toMinutes()
                        initialTime=finalTime
                    }
                    if (status==1){
                        initialTime=finalTime
                    }
                    initialCharge=finalCharge
                    i++
                }
                val list=DischargeDetails(startDate,dischargeLevel, dischargeTime)
                listData.add(list)

            }
            else{
                val list=DischargeDetails(startDate,0.0,0)
                listData.add(list)
            }
            val now=LocalDateTime.now()

            val formatDateTime = now.format(formatter)

//
            val timeNow=LocalDateTime.parse(formatDateTime,formatter)
            if (LocalDateTime.parse(endDate,formatter)>timeNow){
                break
            }
            startDate=endDate
        }




        return listData
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun countsCal(context: Context):Counts{

        var counterObj=Counts(0,0,0)
        db = Room.databaseBuilder(
            context,
            BatteryDB::class.java, "BatteryDB"
        ).allowMainThreadQueries().build()
        val datas:List<BatteryUsage> =db.batteryUsageDao().getAll()
        var startTime=datas[0].timestamp
        var endTime=datas[0].timestamp

        var i=0
        var flag=0
        while (i<datas.count()){
            flag=0
            var status=datas[i].batteryStatus
            if(status==1){
                flag=1
                if (datas[i].batteryPercent.toDouble()==100.0){
                    flag=2
                    startTime=datas[i].timestamp
                    endTime=startTime

                }
                i++
                while (i<datas.count()){
                    status=datas[i].batteryStatus
                    if(status==1 && datas[i].batteryPercent.toDouble()==100.0 && flag==1){
                        flag=2
                        startTime=datas[i].timestamp
                        endTime=startTime

                    }
                    else if (flag==2 && datas[i].batteryPercent.toDouble()==100.0){
                        endTime=datas[i].timestamp
                    }
                    if (status==0){
                        break
                    }

                    i++
                }


            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val initialTime=LocalDateTime.parse(startTime,formatter)
            val finalTime=LocalDateTime.parse(endTime,formatter)
            var duration:Duration = Duration.between(initialTime,finalTime)
            if (flag==2 && duration.toMinutes()>=30){
                counterObj.BadCount++
            }
            else if (flag==2 && duration.toMinutes()<30){
                counterObj.OptimalCount++
            }
            else if (flag==1){
                counterObj.SpotCount++
            }

            i++

        }

    return counterObj
    }
}