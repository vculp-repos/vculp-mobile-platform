package com.example.vculp.features.chooseLocation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.network.FavRegionRequest
import com.example.vculp.network.FavRegionUpdateRequest
import com.example.vculp.network.NetworkImpl
import com.example.vculp.network.RetrofitBuilder
import com.example.vculp.network.RetrofitBuilder.networkService
import com.example.vculp.shared.data.models.FareRecommendationData
import com.example.vculp.shared.data.models.FavRegionData
import com.example.vculp.shared.data.models.FavRegionDataItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class FavLocationsRepository(private val dao: FavLocationsDao) {
    private val networkService = NetworkImpl(RetrofitBuilder.networkService)
    var favLocations = dao.getAll()
    val home = dao.getHome()
    val office = dao.getOffice()

    suspend fun getAllLocations(userId: String, pageNumber: Int?, pageSize: Int?) {
        try {
            val fetchedLocations = networkService.getAllFavRegion(userId, pageNumber, pageSize)

            fetchedLocations?.let { fetchedData: List<FavRegionDataItem> ->
                val gson = Gson()
                val dataItemsJson = gson.toJsonTree(fetchedData)
                val dataItems: List<FavRegionDataItem> = gson.fromJson(dataItemsJson, object : TypeToken<List<FavRegionDataItem>>() {}.type)
                dao.updateFavLocations(dataItems)
            }
            favLocations = dao.getAll()
        } catch (e: Exception) {
            Log.i("favregion", "getAllLocations: ${e.message}")
        }
    }

    suspend fun insert(favLocation: FavLocation) {

        val faveLocation = FavRegionRequest(
            areaName = favLocation.address,
            name = favLocation.title,
            radius = 0,
            longitude = favLocation.longitude,
            latitude = favLocation.latitude,
            userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )

        val favRegionDataItem = networkService.createFavRegion(faveLocation)

        Log.i("favregion", "insert: ${favRegionDataItem?.favoriteRegionId}")

        if (favRegionDataItem != null) {
            dao.insert(favRegionDataItem)
        }
    }

    suspend fun update(favLocation: FavRegionDataItem) {
        val reqLocation = FavRegionUpdateRequest(
            areaName = favLocation.areaName,
            name = favLocation.name,
            radius = favLocation.radius,
            longitude = favLocation.longitude,
            latitude = favLocation.latitude,
            isActive = favLocation.isActive
        )

        val updatedRegion =
            networkService.updateFavRegion(favRegionId = favLocation.favoriteRegionId, reqLocation)

        if (updatedRegion != null) {
            dao.update(updatedRegion)
        } else {
            throw Exception("failed to update!")
        }
    }

    suspend fun delete(favLocation: FavRegionDataItem) {
        val deletedItem = networkService.deleteFavRegion(favLocation.favoriteRegionId)
        if (deletedItem) {
            dao.delete(favLocation)
        }
    }
}