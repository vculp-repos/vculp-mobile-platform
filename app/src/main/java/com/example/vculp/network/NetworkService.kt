package com.example.vculp.network

import com.example.vculp.shared.data.models.FareRecommendationItem
import com.example.vculp.shared.data.models.UserLocation
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

data class FareRecommendationRequest(
    val origin: String,
    val destination: String,
    val vehicleType: String,
    val vehicleBodyType: String,
    val vehicleNoOfSeater: Int
)
interface NetworkService {
    @POST("/fareRecommender-api/fareRecommendation")
    @Headers("Content-Type: application/json")
    suspend fun getFareRecommendation(@Body request: FareRecommendationRequest): FareRecommendationItem
}