package com.example.taskit

import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ClassActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

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

        // Initialize the SupportMapFragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            Log.e("ERROR", "SupportMapFragment is null!")
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

}
