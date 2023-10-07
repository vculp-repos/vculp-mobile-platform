package com.example.vculp.network

import com.example.vculp.shared.data.models.DriverApiData
import com.example.vculp.shared.data.models.UserLocation
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserLocationService {

    @POST("/updateLocation")
    suspend fun updateLiveLocation(@Query("location") location: UserLocation)

    @GET("/getDriversList")
    suspend fun getDriversList(@Query("start_location") startLocation: String,@Query("end_location") endLocation: String): DriverApiData
}