package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class DriverApiDataItem(
    @SerializedName("imageUri")
    val imageUri: String,
    @SerializedName("lattitude")
    val lattitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("ridePrice")
    val ridePrice: String,
    @SerializedName("vehicleNo")
    val vehicleNo: String,
    @SerializedName("vehicleType")
    val vehicleType: String
)