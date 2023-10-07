package com.example.vculp.network

import com.example.vculp.shared.data.models.DriverApiData
import com.example.vculp.shared.data.models.UserLocation

class UserLocationImpl(private val userLocationService: UserLocationService): UserLocationHelper {
    override suspend fun updateLocation(location: UserLocation) {
        userLocationService.updateLiveLocation(location)
    }

    override suspend fun getDriversList(startLocation: String, endLocation: String): DriverApiData {
        return userLocationService.getDriversList(startLocation,endLocation)
    }

}