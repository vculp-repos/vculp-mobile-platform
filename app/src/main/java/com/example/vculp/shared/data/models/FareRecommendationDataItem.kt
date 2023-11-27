package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class FareRecommendationDataItem(
    @SerializedName("actualDistanceAfterFreeKms")
    val actualDistanceAfterFreeKms: Int,
    @SerializedName("baseFare")
    val baseFare: Int,
    @SerializedName("baseFareFreeKms")
    val baseFareFreeKms: Int,
    @SerializedName("createdByUserId")
    val createdByUserId: Int,
    @SerializedName("createdByUserName")
    val createdByUserName: String,
    @SerializedName("creationTime")
    val creationTime: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("durationFare")
    val durationFare: Int,
    @SerializedName("lastUpdated")
    val lastUpdated: String,
    @SerializedName("lastUpdatedByUserId")
    val lastUpdatedByUserId: Int,
    @SerializedName("lastUpdatedByUserName")
    val lastUpdatedByUserName: String,
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("minimumDistanceFare")
    val minimumDistanceFare: Int,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("recommendedDistanceFare")
    val recommendedDistanceFare: Int,
    @SerializedName("tollCharges")
    val tollCharges: Int,
    @SerializedName("vehicleTypeId")
    val vehicleTypeId: String
)