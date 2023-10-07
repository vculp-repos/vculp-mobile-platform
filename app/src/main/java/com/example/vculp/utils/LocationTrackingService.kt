package com.example.vculp.utils

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.vculp.R
import com.example.vculp.network.RetrofitBuilder
import com.example.vculp.network.UserLocationHelper
import com.example.vculp.network.UserLocationImpl
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationTrackingService : Service() {

    private lateinit var userLocationViewModel: UserLocationViewModel
    private lateinit var locationManager: CustomLocationManager
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        userLocationViewModel = UserLocationViewModel.getInstance()
        locationManager = CustomLocationManager(this)

        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }
    private fun start(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notification = Notification.Builder(this, "liveride_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("live tracking ride")
                .build()
            val userLocationHelper = UserLocationImpl(RetrofitBuilder.userLocationService)
            startLocationUpdates(userLocationHelper)
            startForeground(1, notification)
        }
    }



    enum class Actions {
        START, STOP
    }

    private fun startLocationUpdates(userLocationHelper: UserLocationImpl) {
       locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    userLocationViewModel.updateLocation(location)
                    userLocationViewModel.updateRemoteLocation(userLocationHelper)
                }
            }
        }
        val coroutineScope = MainScope()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                locationManager.startLocationUpdates(locationCallback,mainLooper)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(this::locationCallback.isInitialized) locationManager.stopLocationUpdates(locationCallback)
    }
}