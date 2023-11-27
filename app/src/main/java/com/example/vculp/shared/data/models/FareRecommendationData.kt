package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class FareRecommendationData(
    @SerializedName("value")
    val value: List<FareRecommendationDataItem>
)