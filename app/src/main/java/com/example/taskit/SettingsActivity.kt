package com.example.taskit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var defaultColorOption: LinearLayout
    private lateinit var tealColorOption: LinearLayout
    private lateinit var orangeCheckIcon: View
    private lateinit var tealCheckIcon: View
    private lateinit var darkModeToggle: SwitchCompat

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_add-> {
                    // Open AddClassActivity
                    val intent = Intent(this, AddClassActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_settings -> {
                    // Open SettingsActivity
                    recreate()
                }
            }
            true
        }

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("SettingsPreferences", MODE_PRIVATE)

        // Initialize views
        defaultColorOption = findViewById(R.id.default_color_option)
        tealColorOption = findViewById(R.id.teal_color_option)
        orangeCheckIcon = findViewById(R.id.orange_check_icon)
        tealCheckIcon = findViewById(R.id.teal_check_icon)
        darkModeToggle = findViewById(R.id.dark_mode_toggle)

        // Restore saved preferences
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val selectedColor = sharedPreferences.getString("theme_color", "orange")

        // Apply saved theme color
        applyThemeColor(selectedColor)

        // Apply saved dark mode setting
        darkModeToggle.isChecked = isDarkMode
        if (isDarkMode) enableDarkMode() else disableDarkMode()

        // Set listeners for theme color options
        defaultColorOption.setOnClickListener {
            applyThemeColor("orange")
            savePreferences("theme_color", "orange")
        }

        tealColorOption.setOnClickListener {
            applyThemeColor("teal")
            savePreferences("theme_color", "teal")
        }

        // Set listener for dark mode toggle
        darkModeToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
                savePreferences("dark_mode", true)
            } else {
                disableDarkMode()
                savePreferences("dark_mode", false)
            }
        }
    }

    private fun applyThemeColor(color: String?) {
        val settingsTitle = findViewById<TextView>(R.id.settings_title)
        when (color) {
            "orange" -> {
                orangeCheckIcon.visibility = View.VISIBLE
                tealCheckIcon.visibility = View.INVISIBLE
                Toast.makeText(this, "Orange Theme Applied", Toast.LENGTH_SHORT).show()
                settingsTitle.backgroundTintList = getColorStateList(R.color.orange)

            }
            "teal" -> {
                orangeCheckIcon.visibility = View.INVISIBLE
                tealCheckIcon.visibility = View.VISIBLE
                Toast.makeText(this, "Teal Theme Applied", Toast.LENGTH_SHORT).show()
                settingsTitle.backgroundTintList = getColorStateList(R.color.teal)
            }
        }
    }

    private fun enableDarkMode() {
        val settingsLayout = findViewById<LinearLayout>(R.id.settings_layout)
        window.decorView.setBackgroundColor(getColor(android.R.color.background_dark))
        Toast.makeText(this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show()
        settingsLayout.setBackgroundColor(getColor(R.color.background_dark))
        val darkModeTitle = findViewById<TextView>(R.id.dark_mode_title)
        val themeColorTitle = findViewById<TextView>(R.id.theme_color_title)
        darkModeTitle.setTextColor(getColor(android.R.color.white))
        themeColorTitle.setTextColor(getColor(android.R.color.white))
    }

    private fun disableDarkMode() {
        window.decorView.setBackgroundColor(getColor(android.R.color.background_light))
        Toast.makeText(this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show()
        val settingsLayout = findViewById<LinearLayout>(R.id.settings_layout)
        settingsLayout.setBackgroundColor(getColor(R.color.background_light))
        val darkModeTitle = findViewById<TextView>(R.id.dark_mode_title)
        val themeColorTitle = findViewById<TextView>(R.id.theme_color_title)
        darkModeTitle.setTextColor(getColor(R.color.black))
        themeColorTitle.setTextColor(getColor(R.color.black))
    }

    private fun savePreferences(key: String, value: Any) {
        with(sharedPreferences.edit()) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
            }
            apply()
        }
    }
}