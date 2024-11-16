package com.example.taskit

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_class)

        val className = intent.getStringExtra("class_name")
        val teacher = intent.getStringExtra("teacher")
        val location = intent.getStringExtra("location")
        val time = intent.getStringExtra("time")

        // Find the TextView and update its content
        val classNameText = findViewById<TextView>(R.id.class_name)
        val infoText = findViewById<TextView>(R.id.info_text)
        infoText.text = """
            Teacher: $teacher
            Location: $location
            Time: $time
        """.trimIndent()
        classNameText.text = className
    }
}
