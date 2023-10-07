package com.example.vculp.network

import com.example.vculp.shared.data.models.DriverApiData
import com.example.vculp.shared.data.models.UserLocation

interface UserLocationHelper {
    suspend fun updateLocation(location: UserLocation)
    suspend fun getDriversList(startLocation: String,endLocation: String): DriverApiData
}