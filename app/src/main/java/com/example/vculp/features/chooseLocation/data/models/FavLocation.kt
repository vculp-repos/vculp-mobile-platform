package com.example.vculp.features.chooseLocation.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



data class FavLocation(
    var title: String,
    var address: String,
    val longitude: Double,
    val latitude: Double,
)
