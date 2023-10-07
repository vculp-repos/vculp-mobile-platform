package com.example.vculp.features.chooseLocation.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vculp.features.chooseLocation.data.models.FavLocation

@Dao
interface FavLocationsDao {

    @Insert
    suspend fun insert(favLocation: FavLocation)

    @Update
    suspend fun update(favLocation: FavLocation)

    @Delete
    suspend fun delete(favLocation: FavLocation)

    @Query("SELECT * FROM fav_locations_table ORDER BY location_title")
    fun getAll(): LiveData<List<FavLocation>>

    @Query("SELECT * FROM fav_locations_table WHERE location_title LIKE 'Home' OR location_title LIKE 'House'")
    fun getHome(): LiveData<FavLocation>

    @Query("SELECT * FROM fav_locations_table WHERE location_title LIKE 'Office' OR location_title LIKE 'Work'")
    fun getOffice(): LiveData<FavLocation>

}