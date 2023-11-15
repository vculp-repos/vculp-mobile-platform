package com.example.vculp.network

import com.example.vculp.shared.data.models.FareRecommendationItem

class NetworkImpl(private val networkService: NetworkService): NetworkHelper {
    override suspend fun getFareRecommendation(origin: String, destination: String, vehicleType: String, vehicleBodyType: String, vehicleNoOfSeater: Int ): FareRecommendationItem {
        val request = FareRecommendationRequest(
            origin, destination, vehicleType, vehicleBodyType, vehicleNoOfSeater
        )

        return networkService.getFareRecommendation(request)
    }
}