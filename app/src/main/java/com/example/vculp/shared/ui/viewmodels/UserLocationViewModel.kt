package com.example.vculp.shared.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vculp.shared.data.models.UserLocation
import com.example.vculp.network.UserLocationHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.lang.Exception

class UserLocationViewModel : ViewModel() {
    companion object {
        private val viewModel by lazy { UserLocationViewModel() }
        fun getInstance() = viewModel
    }

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun updateLocation(location: Location) {
        _currentLocation.value = location
    }

    fun updateRemoteLocation(userLocationHelper: UserLocationHelper){
        viewModelScope.launch {
            try{
                userLocationHelper.updateLocation(
                    UserLocation(LatLng(_currentLocation.value!!.latitude,
                    _currentLocation.value!!.longitude
                ))
                )
            } catch (e: Exception){
                Log.i("location", "updateRemoteLocation: couldn't make request ${e.message}")
            }
        }
    }
}