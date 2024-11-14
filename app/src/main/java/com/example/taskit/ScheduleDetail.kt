package com.example.taskit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScheduleDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

        // Retrieve data from intent
        val className = intent.getStringExtra("className")
        val teacher = intent.getStringExtra("teacher")
        val location = intent.getStringExtra("location")
        val time = intent.getStringExtra("time")

//        // Set data to TextViews
//        findViewById<TextView>(R.id.classs_name).text = className
//        findViewById<TextView>(R.id.info_text).text = teacher + "\n" + location + "\n" + time;
    }
}
