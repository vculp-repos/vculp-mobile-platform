package com.example.vculp.features.chooseLocation.data
import com.example.vculp.features.chooseLocation.data.models.FavLocation

class FavLocationsRepository(private val dao: FavLocationsDao) {
    val favLocations = dao.getAll()
    val home = dao.getHome()
    val office = dao.getOffice()

    suspend fun insert(favLocation: FavLocation){
        dao.insert(favLocation)
    }

    suspend fun update(favLocation: FavLocation){
        dao.update(favLocation)
    }

    suspend fun delete(favLocation: FavLocation){
        dao.delete(favLocation)
    }
}