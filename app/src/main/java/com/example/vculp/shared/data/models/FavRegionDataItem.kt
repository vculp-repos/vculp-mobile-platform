package com.example.vculp.shared.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_locations_table")
data class FavRegionDataItem(

    @PrimaryKey()
    @ColumnInfo(name = "favoriteRegionId")
    @SerializedName("favoriteRegionId")
    val favoriteRegionId: String,
    @ColumnInfo(name = "areaName")
    @SerializedName("areaName")
    var areaName: String,
    @ColumnInfo("latitude")
    @SerializedName("latitude")
    val latitude: Double,
    @ColumnInfo("links")
    @SerializedName("links")
    val links: List<Link>,
    @ColumnInfo("longitude")
    @SerializedName("longitude")
    val longitude: Double,
    @ColumnInfo("name")
    @SerializedName("name")
    var name: String,
    @ColumnInfo("radius")
    @SerializedName("radius")
    val radius: Int,
    @ColumnInfo("isActive")
    @SerializedName("isActive")
    val isActive: Boolean = true
)