package com.example.taskit

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.taskit.database.viewmodel.ClassViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val classViewModel: ClassViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val addClassButton = findViewById<ImageButton>(R.id.add_class_button)

        // Setup notification channel
        createNotificationChannel()

        // Check and request notification permissions for Android 13+
        checkNotificationPermission()

        // Redirect to AddClassActivity on button click
        addClassButton.setOnClickListener {
            val intent = Intent(this, AddClassActivity::class.java)
            startActivity(intent)
        }

        // Setup bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    recreate()
                    true
                }
                R.id.nav_add -> {
                    val intent = Intent(this, AddClassActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Set the month title to the current month
        val monthTitle = findViewById<TextView>(R.id.month_title)
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        monthTitle.text = monthFormat.format(calendar.time)

        // Populate days of the week and highlight the current day
        populateDaysAndDates()
        highlightCurrentDate(calendar.get(Calendar.DAY_OF_MONTH))

        // Observe classes and schedule notifications
        observeAndScheduleNotifications()

        // Apply theme and dark mode
        sharedPreferences = getSharedPreferences("SettingsPreferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val selectedColor = sharedPreferences.getString("theme_color", "orange")
        applyThemeColor(selectedColor)
        applyDarkMode(isDarkMode)
    }

    private fun applyThemeColor(color: String?) {
        if (color == null) return

        val topSection = findViewById<RelativeLayout>(R.id.top_section)
        val bottomSection = findViewById<LinearLayout>(R.id.bottom_section)

        when (color) {
            "orange" -> {
                topSection.backgroundTintList = getColorStateList(R.color.orange)
                bottomSection.backgroundTintList = getColorStateList(R.color.orange)
            }
            "teal" -> {
                topSection.backgroundTintList = getColorStateList(R.color.teal)
                bottomSection.backgroundTintList = getColorStateList(R.color.teal)
            }
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        val background = findViewById<LinearLayout>(R.id.main)

        if (isDarkMode) {
            background.setBackgroundResource(R.color.background_dark)
        } else {
            background.setBackgroundResource(R.color.white)
        }
    }

    private fun observeAndScheduleNotifications() {
        val classCardsContainer = findViewById<LinearLayout>(R.id.class_cards_container)

        classViewModel.allClasses.observe(this, Observer { classes ->
            classCardsContainer.removeAllViews()

            classes.forEach { classEntity ->
                addClassCard(
                    ClassItem(
                        type = classEntity.type,
                        title = classEntity.title,
                        startTime = classEntity.startTime,
                        endTime = classEntity.endTime,
                        day = classEntity.day,
                        room = classEntity.room,
                        teacher = classEntity.teacher,
                        todo = classEntity.todo,
                        deadlines = classEntity.deadlines,
                        notes = classEntity.notes
                    ),
                    classCardsContainer
                )
            }

            // Schedule notifications for all classes
            scheduleNotificationsForClasses(this, classes)
        })
    }

    private fun populateDaysAndDates() {
        val daysOfWeekContainer = findViewById<LinearLayout>(R.id.days_of_week)
        val datesContainer = findViewById<LinearLayout>(R.id.dates)

        daysOfWeekContainer.removeAllViews()
        datesContainer.removeAllViews()

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())

        for (i in 0..6) {
            val dayTextView = TextView(this).apply {
                text = dayFormat.format(calendar.time)
                setTextColor(getColor(android.R.color.white))
                textSize = 28f
                setPadding(16, 0, 16, 0)
                typeface = resources.getFont(R.font.fredoka_condensedlight)
            }
            daysOfWeekContainer.addView(dayTextView)

            val dateTextView = TextView(this).apply {
                text = dateFormat.format(calendar.time)
                setTextColor(getColor(android.R.color.white))
                textSize = 28f
                setPadding(26, 8, 26, 8)
                typeface = resources.getFont(R.font.fredoka_condensedlight)
            }
            datesContainer.addView(dateTextView)

            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }

    private fun highlightCurrentDate(currentDay: Int) {
        val datesContainer = findViewById<LinearLayout>(R.id.dates)

        for (i in 0 until datesContainer.childCount) {
            val dateTextView = datesContainer.getChildAt(i) as TextView
            if (dateTextView.text.toString() == currentDay.toString()) {
                dateTextView.setBackgroundResource(R.drawable.circle_shape)
                dateTextView.setTextColor(getColor(android.R.color.black))
                dateTextView.setPadding(30, 8, 30, 8)
            }
        }
    }

    private fun addClassCard(classItem: ClassItem, container: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.class_card, container, false)

        val className = cardView.findViewById<TextView>(R.id.class_name)
        val classDetails = cardView.findViewById<TextView>(R.id.class_details)

        className.text = classItem.name
        classDetails.text =
            "${classItem.teacher} - ${classItem.location} - ${classItem.startTime} to ${classItem.endTime}"

        cardView.setOnClickListener {
            val intent = Intent(this, ClassActivity::class.java).apply {
                putExtra("type", classItem.type)
                putExtra("title", classItem.title)
                putExtra("startTime", classItem.startTime)
                putExtra("endTime", classItem.endTime)
                putExtra("day", classItem.day)
                putExtra("room", classItem.room)
                putExtra("teacher", classItem.teacher)
                putExtra("todo", classItem.todo)
                putExtra("deadlines", classItem.deadlines)
                putExtra("notes", classItem.notes)
            }
            startActivity(intent)
        }

        when (classItem.type) {
            "Course" -> cardView.setBackgroundResource(R.drawable.course_card)
            "Lab." -> cardView.setBackgroundResource(R.drawable.lab_card)
            "Seminar" -> cardView.setBackgroundResource(R.drawable.seminar_card)
            "Other" -> cardView.setBackgroundResource(R.drawable.other_card)
        }

        container.addView(cardView)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "class_channel",
                "Class Notifications",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming classes"
            }

            val notificationManager = getSystemService(android.app.NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun scheduleNotificationsForClasses(context: Context, classes: List<ClassEntity>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()

        for (classEntity in classes) {
            try {
                val dayOfWeek = when (classEntity.day) {
                    "Monday" -> Calendar.MONDAY
                    "Tuesday" -> Calendar.TUESDAY
                    "Wednesday" -> Calendar.WEDNESDAY
                    "Thursday" -> Calendar.THURSDAY
                    "Friday" -> Calendar.FRIDAY
                    "Saturday" -> Calendar.SATURDAY
                    "Sunday" -> Calendar.SUNDAY
                    else -> continue
                }

                val timeParts = classEntity.startTime.split(":")
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()

                calendar.apply {
                    set(Calendar.DAY_OF_WEEK, dayOfWeek)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    if (timeInMillis < System.currentTimeMillis()) {
                        add(Calendar.WEEK_OF_YEAR, 1)
                    }
                }

                val notificationTime = calendar.timeInMillis - (2 * 1000)

                if (notificationTime > System.currentTimeMillis()) {
                    val intent = Intent(context, NotificationReceiver::class.java).apply {
                        putExtra("title", classEntity.title)
                        putExtra("room", classEntity.room)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        classEntity.id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        notificationTime,
                        pendingIntent
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    data class ClassItem(
        val type: String,
        val title: String,
        val startTime: String,
        val endTime: String,
        val day: String,
        val room: String,
        val teacher: String,
        val todo: String,
        val deadlines: String,
        val notes: String
    ) {
        val name: String
            get() = if (title.isEmpty()) type else title

        val location: String
            get() = if (room.isEmpty()) "Unknown Room" else room
    }
}
