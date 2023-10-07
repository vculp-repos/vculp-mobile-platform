package com.example.vculp.shared.data.models

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class UserLocation(
    @SerializedName("location")
    var currentLocation: LatLng
)
