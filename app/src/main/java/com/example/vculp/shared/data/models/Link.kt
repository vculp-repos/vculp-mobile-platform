package com.example.vculp.shared.data.models


import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("href")
    val href: String,
    @SerializedName("method")
    val method: String,
    @SerializedName("rel")
    val rel: String
)