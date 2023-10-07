package com.example.vculp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class PermissionsHandler {
    fun requestNotificationPermissions( context: Context, activity: AppCompatActivity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                10
            )
        }
    }

    fun requestCallPermission(context: Context, activity: AppCompatActivity){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) return ActivityCompat.requestPermissions(
            activity,
          arrayOf(
                Manifest.permission.CALL_PHONE
            ), Constants.CALL_PERMISSION_CODE
        )
    }

    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), Constants.LOCATION_PERMISSION_CODE
        )
    }

}