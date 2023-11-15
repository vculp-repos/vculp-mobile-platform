package com.example.vculp.network

import com.example.vculp.shared.data.models.FareRecommendationItem

interface NetworkHelper {
    suspend fun getFareRecommendation(origin: String, destination: String, vehicleType: String, vehicleBodyType: String, vehicleNoOfSeater: Int ) : FareRecommendationItem
}