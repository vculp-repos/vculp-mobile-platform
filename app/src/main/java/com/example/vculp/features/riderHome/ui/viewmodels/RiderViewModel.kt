package com.example.vculp.features.riderHome.ui.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.shared.data.models.Duration
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class RiderViewModel : ViewModel() {
    companion object {
        private val viewModel by lazy { RiderViewModel() }
        fun getInstance() = viewModel
    }

    val startLocation = MutableLiveData<String>()
    val dropLocation = MutableLiveData<String>()
    val coords = MutableLiveData<Array<LatLng>?>()
    val duration = MutableLiveData<Duration>()

    init {
        startLocation.value = ""
        dropLocation.value = ""
        duration.value = Duration()
    }

    fun setCoords(context: Context){
            val currentLocationCoords = startLocation.value?.let { getCoords(it,context) }
            val dropLocationCoords = dropLocation.value?.let { getCoords(it, context ) }
            if(currentLocationCoords != null && dropLocationCoords != null) coords.postValue(arrayOf(currentLocationCoords,dropLocationCoords))
            else{
                coords.postValue(null)
            }
            Log.i("location", "setCoords: $currentLocationCoords $dropLocationCoords")
    }

    fun locationFromCords(liveLocationCoords: LatLng, context: Context): String? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocation(liveLocationCoords.latitude, liveLocationCoords.longitude, 1)
            if(addresses != null){
                val address = addresses[0]
                return(address.locality)
            }else {
                Log.i("location", "setCurrentLocationName: no locaiton found")
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }

    fun getCoords(address:String, context: Context): LatLng? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[0]
                val latitude: Double = address.latitude
                val longitude: Double = address.longitude
                return LatLng(latitude, longitude)
            } else {
                Log.d("FragmentViewModel", "No address found for $address")
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }

    fun setDropLocation(dl: String){
        dropLocation.value = dl
    }

    fun setStartLocation(sl: String){
        startLocation.value = sl
    }

    fun setDuration(d: Duration){
        duration.postValue(d)
    }

}