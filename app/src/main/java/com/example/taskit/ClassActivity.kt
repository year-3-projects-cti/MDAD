package com.example.taskit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClassActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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

        // Initialize the SupportMapFragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            Log.e("ERROR", "SupportMapFragment is null!")
        }

        val getDirectionsButton = findViewById<Button>(R.id.get_directions_button)
        getDirectionsButton.setOnClickListener {
            openMapsChooser()
        }

        val shareButton = findViewById<Button>(R.id.share_button)
        shareButton.setOnClickListener {
            shareLocation()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home-> {
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
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

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

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            // Save the GoogleMap instance
            this.googleMap = googleMap

            // Define a location (e.g., Politehnica București)
            val politehnica = LatLng(44.438465808342194, 26.050158673663066)

            // Add a marker at the location
            googleMap.addMarker(
                MarkerOptions()
                    .position(politehnica)
                    .title("Politehnica București")
            )

            // Move the camera to the location and set zoom level
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(politehnica, 15f))

            // Optional: Enable map controls (zoom, rotate, etc.)
            googleMap.uiSettings.apply{
                isZoomControlsEnabled = true
            }

            // Log or toast a success message
            Toast.makeText(this, "Marker added successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Log any errors
            Log.e("ERROR", "Error in onMapReady: ${e.message}")
        }
    }

    private fun openMapsChooser() {
        try {
            // Define the location (e.g., Politehnica București)
            val latitude = 44.438465808342194
            val longitude = 26.050158673663066
            val locationUri = "geo:$latitude,$longitude?q=$latitude,$longitude(Politehnica București)"

            // Create an intent to open map applications
            val mapIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(locationUri)
            }

            // Force the chooser dialog to appear
            val chooser = Intent.createChooser(mapIntent, "Open with")
            startActivity(chooser)
        } catch (e: Exception) {
            Log.e("ERROR", "Failed to create map intent: ${e.message}")
            Toast.makeText(this, "Unable to open maps chooser", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareLocation() {
        val latitude = 44.438465808342194
        val longitude = 26.050158673663066
        val locationUri = "https://maps.google.com/?q=$latitude,$longitude"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Location")
            putExtra(Intent.EXTRA_TEXT, "Check out this location: $locationUri")
        }

        startActivity(Intent.createChooser(shareIntent, "Share location via"))
    }


}
