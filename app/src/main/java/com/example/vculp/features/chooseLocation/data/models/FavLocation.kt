package com.example.vculp.features.chooseLocation.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "fav_locations_table")
data class FavLocation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    val id: Long,

    @ColumnInfo(name = "location_title")
    var title: String,

    @ColumnInfo(name = "location")
    val location: String,
)
