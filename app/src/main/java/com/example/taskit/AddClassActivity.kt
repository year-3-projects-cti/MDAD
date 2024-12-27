package com.example.taskit

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddClassActivity : AppCompatActivity() {

    private lateinit var teacherInput: EditText
    private lateinit var todoInput: EditText
    private lateinit var deadlinesInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var titleInput: EditText
    private lateinit var startTimeInput: EditText
    private lateinit var endTimeInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class)

        // Initialize all EditTexts
        teacherInput = findViewById(R.id.teacher_input)
        todoInput = findViewById(R.id.todo_input)
        deadlinesInput = findViewById(R.id.deadlines_input)
        notesInput = findViewById(R.id.notes_input)
        titleInput = findViewById(R.id.title_input)
        startTimeInput = findViewById(R.id.start_time)
        endTimeInput = findViewById(R.id.end_time)

        // Initialize Cancel and Add Buttons
        val cancelButton: TextView = findViewById(R.id.cancel_button)
        val addButton: TextView = findViewById(R.id.add_button)

        // Handle cancel button click
        cancelButton.setOnClickListener {
            finish() // Close the activity
        }

        // Handle add button click
        addButton.setOnClickListener {
            handleAddClass()
        }

        // Handle Bottom Navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        setupBottomNavigation(bottomNavigation)
    }

    private fun handleAddClass() {
        // Gather inputs
        val title = titleInput.text.toString()
        val teacher = teacherInput.text.toString()
        val todo = todoInput.text.toString()
        val deadlines = deadlinesInput.text.toString()
        val notes = notesInput.text.toString()
        val startTime = startTimeInput.text.toString()
        val endTime = endTimeInput.text.toString()

        // Validate inputs
        if (title.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Display the gathered data (for testing; replace with database storage or API call)
        val message = """
            Title: $title
            Teacher: $teacher
            Start Time: $startTime
            End Time: $endTime
            To-Do: $todo
            Deadlines: $deadlines
            Notes: $notes
        """.trimIndent()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        // Close the activity after saving
        finish()
    }

    private fun setupBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show()
                    // Navigate to HomeActivity
                    finish() // Replace this with navigation logic if needed
                    true
                }
                R.id.nav_add -> {
                    Toast.makeText(this, "You are already here.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
                    // Navigate to SettingsActivity
                    true
                }
                else -> false
            }
        }
    }
}