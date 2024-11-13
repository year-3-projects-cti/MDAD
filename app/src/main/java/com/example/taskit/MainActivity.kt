package com.example.taskit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the month title to the current month
        val monthTitle = findViewById<TextView>(R.id.month_title)
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        monthTitle.text = monthFormat.format(calendar.time)

        // Highlight the current date
        highlightCurrentDate(calendar.get(Calendar.DAY_OF_MONTH))
    }

    private fun highlightCurrentDate(currentDay: Int) {
        // This function should dynamically create the TextViews for each day
        // and set the style for the current day based on `currentDay` parameter.
        // Implement this with a loop that generates date views as shown in the XML.
    }
}