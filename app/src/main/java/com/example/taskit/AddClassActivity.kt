package com.example.taskit

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.taskit.database.viewmodel.ClassViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddClassActivity : AppCompatActivity() {

    private lateinit var teacherInput: EditText
    private lateinit var todoInput: EditText
    private lateinit var deadlinesInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var titleInput: EditText
    private lateinit var startTimeInput: EditText
    private lateinit var endTimeInput: EditText

    private val classViewModel: ClassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class)

        teacherInput = findViewById(R.id.teacher_input)
        todoInput = findViewById(R.id.todo_input)
        deadlinesInput = findViewById(R.id.deadlines_input)
        notesInput = findViewById(R.id.notes_input)
        titleInput = findViewById(R.id.title_input)
        startTimeInput = findViewById(R.id.start_time)
        endTimeInput = findViewById(R.id.end_time)

        val cancelButton: TextView = findViewById(R.id.cancel_button)
        val addButton: TextView = findViewById(R.id.add_button)

        cancelButton.setOnClickListener {
            finish()
        }

        addButton.setOnClickListener {
            handleAddClass()
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        setupBottomNavigation(bottomNavigation)
    }

    private fun handleAddClass() {
        val title = titleInput.text.toString()
        val teacher = teacherInput.text.toString()
        val todo = todoInput.text.toString()
        val deadlines = deadlinesInput.text.toString()
        val notes = notesInput.text.toString()
        val startTime = startTimeInput.text.toString()
        val endTime = endTimeInput.text.toString()

        if (title.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val newClass = ClassEntity(
            name = title,
            teacher = teacher,
            location = "",
            time = "$startTime - $endTime"
        )

        CoroutineScope(Dispatchers.IO).launch {
            classViewModel.insertClass(newClass)
            runOnUiThread {
                Toast.makeText(this@AddClassActivity, "Class added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupBottomNavigation(bottomNavigation: BottomNavigationView) {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show()
                    finish() // Navigare spre Home
                    true
                }
                R.id.nav_add -> {
                    Toast.makeText(this, "You are already here.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
                    // Navigare spre Settings
                    true
                }
                else -> false
            }
        }
    }
}
