package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class FareRecommendationItem(
    @SerializedName("value")
    val value: List<Value>
)