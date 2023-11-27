package com.example.vculp.network

import androidx.room.Entity
import com.example.vculp.shared.data.models.FareRecommendationData
import com.example.vculp.shared.data.models.FavRegionData
import com.example.vculp.shared.data.models.FavRegionDataItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class FareRecommendationRequest(
    val origin: String,
    val destination: String,
    val vehicleType: String,
    val vehicleBodyType: String,
    val vehicleNoOfSeater: Int
)


data class FavRegionRequest(
    val name: String,
    val areaName: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val userId: String,
    val isActive: Boolean = true
)

data class FavRegionUpdateRequest(
    val name: String,
    val areaName: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val isActive: Boolean = true
)

interface NetworkService {
    @POST("/fareRecommender-api/fareRecommendation")
    @Headers("Content-Type: application/json")
    suspend fun getFareRecommendation(@Body request: FareRecommendationRequest): FareRecommendationData

    @POST("/favoriteregion-api/favoriteregion")
    @Headers("Content-Type: application/vnd.vculp.favoriteregion.create.v1+json")
    suspend fun createFavRegion(@Body request: FavRegionRequest): FavRegionDataItem

    @GET("/favoriteregion-api/favoriteregion")
    suspend fun getAllFavRegions(
        @Query("userId") userId: String,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): List<FavRegionDataItem>

    @GET("/favoriteregion-api/favoriteregion/{favoriteRegionId}")
    suspend fun getFavRegionById(@Path(value = "favoriteRegionId", encoded = true)  favRegionId: String):FavRegionDataItem

    @PUT("/favoriteregion-api/favoriteregion/{favoriteRegionId}")
    @Headers("Content-Type: application/vnd.vculp.favoriteregion.update.v1+json")
    suspend fun updateFavRegion(@Path(value = "favoriteRegionId", encoded = true)  favRegionId: String, @Body request: FavRegionUpdateRequest):FavRegionDataItem

    @DELETE("/favoriteregion-api/favoriteregion/{favoriteRegionId}")
    suspend fun deleteFavRegion(@Path(value = "favoriteRegionId", encoded = true)  favRegionId: String): Boolean
}