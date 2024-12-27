package com.example.taskit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        // Preluare date din Intent
        val teacher = intent.getStringExtra("teacher") ?: "Unknown Teacher"
        val location = intent.getStringExtra("location") ?: "Unknown Location"
        val time = intent.getStringExtra("time") ?: "Unknown Time"

        // Setare valori în layout
        val classNameTextView = findViewById<TextView>(R.id.class_name)
        val infoTextView = findViewById<TextView>(R.id.info_text)
        val className = prepareClassName(intent.getStringExtra("class_name"));


        classNameTextView.text = className
        infoTextView.text = "Prof. $teacher\n$location\n$time"
    }

    private fun prepareClassName(className: String?): String {
        if (className == null) {
            return "Unknown Class"
        }
        if (className.length <= 10) {
            return className
        }
        val words = className.split(" ")
        val result = StringBuilder()
        for (word in words) {
            result.append(word).append("\n")
        }
        return result.toString()
    }
}
