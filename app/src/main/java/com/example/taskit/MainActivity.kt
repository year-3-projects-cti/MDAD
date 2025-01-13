package com.example.taskit

import com.example.taskit.database.viewmodel.ClassViewModel
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
<<<<<<< Updated upstream
=======
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.taskit.database.viewmodel.ClassViewModel
>>>>>>> Stashed changes
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val classViewModel: ClassViewModel by viewModels() // ViewModel-ul nostru
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val addClassButton = findViewById<ImageButton>(R.id.add_class_button)


        // Redirect to AddClassActivity on click
        addClassButton.setOnClickListener {
            val intent = Intent(this, AddClassActivity::class.java)
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
                    val intent = Intent(this, AddClassActivity::class.java)
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

        observeClasses()
//        classViewModel.emptyClasses()
        sharedPreferences = getSharedPreferences("SettingsPreferences", MODE_PRIVATE)

        // Restore saved preferences
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val selectedColor = sharedPreferences.getString("theme_color", "orange")

        // Apply saved theme color
        applyThemeColor(selectedColor)
        applyDarkMode(isDarkMode)
    }

    override fun onPause() {
        super.onPause()
        sendExitNotification()
    }

    private fun sendExitNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        val notification = NotificationCompat.Builder(this, "class_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("TaskIT Reminder")
            .setContentText("Don't forget to check your schedule!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically removes the notification when clicked
            .build()

        // Display the notification
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }


    private fun applyThemeColor(color: String?) {
        if (color == null) {
            return
        }

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




    private fun observeClasses() {
        val classCardsContainer = findViewById<LinearLayout>(R.id.class_cards_container)

        classViewModel.allClasses.observe(this) { classes: List<ClassEntity> ->
            classCardsContainer.removeAllViews()

            classes.forEach { classEntity ->
                addClassCard(
                    ClassItem(
                        type = classEntity.type,
                        title = classEntity.title,
                        datetime = classEntity.datetime,
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
        }

<<<<<<< Updated upstream
=======
            scheduleNotificationsForClasses(this, classes)
        })
>>>>>>> Stashed changes
    }

    private fun populateDaysAndDates() {
        val daysOfWeekContainer = findViewById<LinearLayout>(R.id.days_of_week)
        val datesContainer = findViewById<LinearLayout>(R.id.dates)

        // Clear any existing views
        daysOfWeekContainer.removeAllViews()
        datesContainer.removeAllViews()

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
            dateTextView.setPadding(26, 8, 26, 8)
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
            val intent = Intent(this, ClassActivity::class.java)
            intent.putExtra("type", classItem.type)
            intent.putExtra("title", classItem.title)
            intent.putExtra("datetime", classItem.datetime)
            intent.putExtra("day", classItem.day)
            intent.putExtra("room", classItem.room)
            intent.putExtra("teacher", classItem.teacher)
            intent.putExtra("todo", classItem.todo)
            intent.putExtra("deadlines", classItem.deadlines)
            intent.putExtra("notes", classItem.notes)

            startActivity(intent)
        }

        // Set the card background color based on the class type
        when (classItem.type) {
            "Course" -> cardView.setBackgroundResource(R.drawable.course_card)
            "Lab." -> cardView.setBackgroundResource(R.drawable.lab_card)
            "Seminar" -> cardView.setBackgroundResource(R.drawable.seminar_card)
            "Other" -> cardView.setBackgroundResource(R.drawable.other_card)
        }

        // Add the card to the container
        container.addView(cardView)
    }

<<<<<<< Updated upstream
=======
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

>>>>>>> Stashed changes
    data class ClassItem(
        val type: String,
        val title: String,
        val datetime: String,
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

        val time: String
            get() = if (datetime.isEmpty()) "Unknown Time" else datetime
    }
}