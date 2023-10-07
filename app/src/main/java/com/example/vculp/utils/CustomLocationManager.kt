package com.example.vculp.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.play.integrity.internal.m
import java.lang.Exception

class CustomLocationManager(private val context: Context) {


    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val permissionsHandler = PermissionsHandler()

    fun checkLocationPermissions(): Boolean {
        // Check if the necessary location permissions are granted
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        return coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(activity: Activity): Task<Location> {
        if(checkLocationPermissions()){
            if(isLocationEnabled()) return fusedLocationProviderClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_LOW_POWER, null)
            else {
                Toast.makeText(context, "please enable location!!", Toast.LENGTH_SHORT).show()
                throw Exception("the location isn't turned on")
            }
        } else {
            permissionsHandler.requestLocationPermissions(activity)
            return fusedLocationProviderClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_LOW_POWER, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(locationCallback: LocationCallback,looper: Looper){

        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // Interval in milliseconds - adjust as needed
            fastestInterval = 3000 // Fastest interval in milliseconds - adjust as needed
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            looper
        )
    }

    fun stopLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
