package com.example.taskit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ClassActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        Log.d("DEBUG", "Activity started, initializing MapView...")

        // Initialize the MapView
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        // Add debug info
        Log.d("DEBUG", "MapView initialized successfully!")

        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d("DEBUG", "onMapReady invoked")
        googleMap = map

        val location = LatLng(44.438465808342194, 26.050158673663066)
        googleMap!!.addMarker(MarkerOptions().position(location).title("Politehnica Bucuresti"))
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        Log.d("DEBUG", "Marker added and camera moved to Politehnica Bucuresti")
    }


    // Lifecycle callbacks for MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
        Log.d("DEBUG", "MapView resumed.")
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        Log.d("DEBUG", "MapView paused.")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        Log.d("DEBUG", "MapView destroyed.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
        Log.d("DEBUG", "MapView low memory warning.")
    }
}
