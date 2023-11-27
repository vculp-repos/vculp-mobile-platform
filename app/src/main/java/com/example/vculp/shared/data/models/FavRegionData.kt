package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class FavRegionData(
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("value")
    val value: List<FavRegionDataItem>
)