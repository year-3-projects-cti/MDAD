package com.example.taskit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val addClassButton = findViewById<ImageButton>(R.id.add_class_button)

        // Redirect to AddClassActivity on click
        addClassButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        // Set up navigation item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
            R.id.nav_home -> {
                recreate()
            }

            R.id.nav_add-> {
                // Open AddClassActivity
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_settings -> {
                // Open SettingsActivity
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
            true
        }

        // Set the month title to the current month
        val monthTitle = findViewById<TextView>(R.id.month_title)
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        monthTitle.text = monthFormat.format(calendar.time)

        // Populate the days of the week and their dates
        populateDaysAndDates()

        // Highlight the current date
        highlightCurrentDate(calendar.get(Calendar.DAY_OF_MONTH))

        //Example for bottom part
        val classes = listOf(
            ClassItem("Math", "Mr. Johnson", "Room 204", "10:00 AM"),
            ClassItem("Physics", "Ms. Smith", "Room 305", "12:00 PM"),
            ClassItem("Chemistry", "Dr. Lee", "Room 101", "2:00 PM")
        )

        // Add class cards to the container
        val classCardsContainer = findViewById<LinearLayout>(R.id.class_cards_container)
        for (classItem in classes) {
            addClassCard(classItem, classCardsContainer)
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    private fun populateDaysAndDates() {
        val daysOfWeekContainer = findViewById<LinearLayout>(R.id.days_of_week)
        val datesContainer = findViewById<LinearLayout>(R.id.dates)

        // Clear any existing views
        daysOfWeekContainer.removeAllViews()
        datesContainer.removeAllViews()

        // Set the calendar to the start of the week (usually Sunday)
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        // Format for displaying abbreviated day names and dates
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // "Sun", "Mon", etc.
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())  // Day number (e.g., "12")

        // Loop through the days of the week
        for (i in 0..6) {
            // Create and set up the TextView for the day of the week
            val dayTextView = TextView(this)
            dayTextView.text = dayFormat.format(calendar.time)
            dayTextView.setTextColor(getColor(android.R.color.white))
            dayTextView.textSize = 28f
            dayTextView.setPadding(16, 0, 16, 0)
            dayTextView.typeface = resources.getFont(R.font.fredoka_condensedlight)
            daysOfWeekContainer.addView(dayTextView)

            // Create and set up the TextView for the date
            val dateTextView = TextView(this)
            dateTextView.text = dateFormat.format(calendar.time)
            dateTextView.setTextColor(getColor(android.R.color.white))
            dateTextView.textSize = 28f
            dateTextView.setPadding(30, 8, 30, 8)
            dateTextView.typeface = resources.getFont(R.font.fredoka_condensedlight)
            datesContainer.addView(dateTextView)

            // Move to the next day
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }

    private fun highlightCurrentDate(currentDay: Int) {
        val datesContainer = findViewById<LinearLayout>(R.id.dates)

        // Loop through each TextView in the dates container
        for (i in 0 until datesContainer.childCount) {
            val dateTextView = datesContainer.getChildAt(i) as TextView
            if (dateTextView.text.toString() == currentDay.toString()) {
                // Apply the highlighted style if it matches the current day
                dateTextView.setBackgroundResource(R.drawable.circle_shape)
                dateTextView.setTextColor(getColor(android.R.color.black))
                dateTextView.setPadding(30, 8, 30, 8)
            }
        }
    }

    private fun addClassCard(classItem: ClassItem, container: LinearLayout) {
        // Inflate the card layout
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.class_card, container, false)

        // Set the class information
        val className = cardView.findViewById<TextView>(R.id.class_name)
        val classDetails = cardView.findViewById<TextView>(R.id.class_details)

        className.text = classItem.name
        classDetails.text = "${classItem.teacher} - ${classItem.location} - ${classItem.time}"

        cardView.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("class_name", classItem.name)
            intent.putExtra("teacher", classItem.teacher)
            intent.putExtra("location", classItem.location)
            intent.putExtra("time", classItem.time)
            startActivity(intent)
        }

        // Add the card to the container
        container.addView(cardView)
    }

    data class ClassItem(val name: String, val teacher: String, val location: String, val time: String)
}