package com.example.vculp.network

import com.example.vculp.shared.data.models.FareRecommendationData
import com.example.vculp.shared.data.models.FavRegionData
import com.example.vculp.shared.data.models.FavRegionDataItem

interface NetworkHelper {
    suspend fun getFareRecommendation(origin: String, destination: String, vehicleType: String, vehicleBodyType: String, vehicleNoOfSeater: Int ) : FareRecommendationData

    suspend fun createFavRegion(favRegionRequest: FavRegionRequest):FavRegionDataItem?

    suspend fun getAllFavRegion(userId: String, pageNumber: Int?, pageSize: Int?): List<FavRegionDataItem>?

    suspend fun getFavRegionById(favRegionId: String): FavRegionDataItem?

    suspend fun updateFavRegion(favRegionId: String, request: FavRegionUpdateRequest): FavRegionDataItem?

    suspend fun deleteFavRegion(favRegionId: String): Boolean
}