package com.example.vculp.network

import android.util.Log
import com.example.vculp.shared.data.models.FareRecommendationData
import com.example.vculp.shared.data.models.FavRegionData
import com.example.vculp.shared.data.models.FavRegionDataItem

class NetworkImpl(private val networkService: NetworkService) : NetworkHelper {
    override suspend fun getFareRecommendation(
        origin: String,
        destination: String,
        vehicleType: String,
        vehicleBodyType: String,
        vehicleNoOfSeater: Int
    ): FareRecommendationData {
        val request = FareRecommendationRequest(
            origin, destination, vehicleType, vehicleBodyType, vehicleNoOfSeater
        )

        return networkService.getFareRecommendation(request)
    }

    override suspend fun createFavRegion(favRegionRequest: FavRegionRequest): FavRegionDataItem? {
        return try {
            networkService.createFavRegion(favRegionRequest)
        } catch (e: Exception) {
            Log.i("favregion", "createFavRegion: ${e.message} ")
            null
        }
    }

    override suspend fun getAllFavRegion(
        userId: String,
        pageNumber: Int?,
        pageSize: Int?
    ): List<FavRegionDataItem>? {
        return try {
            networkService.getAllFavRegions(userId, pageNumber, pageSize)
        } catch (e: Exception) {
            Log.e("favregion", "${e.message}")
            null
        }
    }

    override suspend fun getFavRegionById(favRegionId: String): FavRegionDataItem? {
        return try {
            networkService.getFavRegionById(favRegionId)
        } catch (e: Exception) {
            Log.e("favregion", "${e.message}")
            null
        }
    }

    override suspend fun updateFavRegion(
        favRegionId: String,
        request: FavRegionUpdateRequest
    ): FavRegionDataItem? {
        return try {
            networkService.updateFavRegion(favRegionId, request)
        } catch (e: Exception) {
            Log.e("favregion", "${e.message}")
            null
        }
    }

    override suspend fun deleteFavRegion(favRegionId: String): Boolean {
        return try{ networkService.deleteFavRegion(favRegionId)
        }catch (e: Exception) {
            Log.e("favregion", "${e.message}")
            false
        }
    }
}