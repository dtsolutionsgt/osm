package com.dts.osm

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Location : PBase() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var textCoordinates: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.initbase(savedInstanceState)

        setContentView(R.layout.activity_location)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        textCoordinates = findViewById(R.id.text_coordinates)

        val buttonStartLocation = findViewById<Button>(R.id.button_start_location)
        buttonStartLocation.setOnClickListener {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    try {
                        location?.let {
                            val latitude = it.latitude
                            val longitude = it.longitude
                            val coordinates = "Latitud: $latitude\nLongitud: $longitude"

                            textCoordinates.text = coordinates
                            msgbox(coordinates)

                        } ?: run {
                            val errorMessage = "No se pudo obtener ubicación"
                            textCoordinates.text = errorMessage
                            msgbox(errorMessage)
                        }
                    } catch (e: Exception) {
                        msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
                    }
                }.addOnFailureListener { e ->
                    val errorMessage = "Error al obtener ubicación: ${e.message}"
                    textCoordinates.text = errorMessage
                    msgbox(errorMessage)
                }
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else {
            msgbox("Permiso de ubicación denegado")
        }
    }

    //eventos
    fun doExit(view: View) {
        finish()
    }
}
