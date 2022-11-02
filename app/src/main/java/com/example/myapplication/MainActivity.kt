package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.ListFormatter
import android.os.Build
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.Gravity
import android.view.View
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
        val header=findViewById<TableRow>(R.id.header)
        val tl=findViewById<TableLayout>(R.id.table)

        var cook= Cook()
        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            listData.clear()
            tl.removeAllViews()
            listData=cook.dischargeCal(applicationContext)
            counter=cook.countsCal(applicationContext)
            header.visibility=View.VISIBLE
            optimalCount.visibility=View.VISIBLE
            spotCount.visibility=View.VISIBLE
            badCount.visibility=View.VISIBLE

            button.text="Refresh"


            badCount.setText("Bad Count : " + counter.BadCount.toString())
            optimalCount.setText("Optimal Count : " +counter.OptimalCount.toString())
            spotCount.setText("Spot Count : " +counter.SpotCount.toString())

            for (ls in listData) {
                val newRow=TableRow(this)
                val newText1=TextView(this)
                val newText2=TextView(this)
                val newText3=TextView(this)

                newText1.setText(ls.date_time)
                newText1.width=400
                newText1.setTypeface(null,Typeface.BOLD)
                newText1.setTextColor(Color.DKGRAY)
                newText1.gravity=Gravity.CENTER_HORIZONTAL
                newRow.addView(newText1)
                newText2.setText(ls.discharge_amount.toString())
                newText2.width=300
                newText2.setTypeface(null,Typeface.BOLD)
                newText2.setTextColor(Color.DKGRAY)
                newText2.gravity=Gravity.CENTER_HORIZONTAL
                newRow.addView(newText2)
                newText3.setText(ls.discharge_time.toString())
                newText3.width=350
                newText3.setTypeface(null,Typeface.BOLD)
                newText3.setTextColor(Color.DKGRAY)
                newText3.gravity=Gravity.CENTER_HORIZONTAL
                newRow.addView(newText3)

                tl.addView(newRow)

            }
        }
    }


}