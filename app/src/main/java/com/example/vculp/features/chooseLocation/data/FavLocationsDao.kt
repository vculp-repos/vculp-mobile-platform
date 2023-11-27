package com.example.vculp.features.chooseLocation.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vculp.network.FavRegionRequest
import com.example.vculp.shared.data.models.FavRegionData
import com.example.vculp.shared.data.models.FavRegionDataItem

@Dao
interface FavLocationsDao {

    @Insert
    suspend fun insert(favLocation: FavRegionDataItem)

    @Update
    suspend fun update(favLocation: FavRegionDataItem)

    @Delete
    suspend fun delete(favLocation: FavRegionDataItem)

    @Query("SELECT * FROM fav_locations_table ORDER BY areaName")
    fun getAll(): LiveData<List<FavRegionDataItem>>

    @Query("SELECT * FROM fav_locations_table WHERE areaName LIKE 'Home' OR areaName LIKE 'House'")
    fun getHome(): LiveData<FavRegionDataItem>

    @Query("SELECT * FROM fav_locations_table WHERE areaName LIKE 'Office' OR areaName LIKE 'Work'")
    fun getOffice(): LiveData<FavRegionDataItem>

    @Transaction
    suspend fun updateFavLocations(favLocations: List<FavRegionDataItem>) {
        // Delete all existing data
        deleteAll()

        // Insert the new data
        insertAll(favLocations)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favLocations: List<FavRegionDataItem>)

    @Query("DELETE FROM fav_locations_table")
    suspend fun deleteAll()

}