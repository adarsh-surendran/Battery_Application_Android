package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    private var listData:MutableList<DischargeDetails>  = mutableListOf()
    private var counter:Counts= Counts()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Intent(this, BatteryService::class.java).also { intent ->
            startService(intent)
        }
        val badCount=findViewById<TextView>(R.id.BadCount)
        val optimalCount=findViewById<TextView>(R.id.OptimalCount)
        val spotCount=findViewById<TextView>(R.id.SpotCount)
        val ll=findViewById<TableLayout>(R.id.table)
        var cook= Cook()
        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            listData.clear()
            listData=cook.dischargeCal(applicationContext)
            counter=cook.countsCal(applicationContext)
            ll.removeAllViews()
            button.text="Refresh"


            badCount.setText("Bad Count : " + counter.BadCount.toString())
            optimalCount.setText("Optimal Count : " +counter.OptimalCount.toString())
            spotCount.setText("Spot Count : " +counter.SpotCount.toString())
            var count=0
            for (ls in listData) {
                val row = TableRow(this)
                val lp: TableRow.LayoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                row.layoutParams = lp
                var date_time_ = TextView(this)
                var discharge_ = TextView(this)
                var time_ = TextView(this)
                date_time_.setText(ls.date_time+"     ")
                discharge_.setText(ls.discharge_amount.toString()+"              ")
                time_.setText(ls.discharge_time.toString())
                row.addView(date_time_)
                row.addView(discharge_)
                row.addView(time_)
                ll.addView(row, count)
                count++
            }
        }
    }


}