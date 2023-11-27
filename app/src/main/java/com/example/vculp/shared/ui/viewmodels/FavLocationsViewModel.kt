package com.example.vculp.shared.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.shared.data.models.FavRegionDataItem
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavLocationsViewModel(private val repository: FavLocationsRepository) : ViewModel() {


    val favLocations = repository.favLocations
    val home = repository.home
    val office = repository.office
    val selectedLocation = MutableLiveData<FavRegionDataItem?>()

    fun setSelectedLocation(location: FavRegionDataItem?) {
        selectedLocation.value = location
    }

    fun getAllLocations() = viewModelScope.launch(IO) {
        val userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        val pageNumber = null
        val pageSize = null
        repository.getAllLocations(userId, pageNumber, pageSize)
    }

    fun insert(favLocation: FavLocation) = viewModelScope.launch(IO) {
        repository.insert(favLocation)
    }

    fun update(favLocation: FavRegionDataItem) = viewModelScope.launch(IO) {
        try {
            repository.update(favLocation)
        } catch (exception: Exception) {
            Log.e("favRegion", "Failed to update: ${exception.message}")
        }
    }

    fun delete(favLocation: FavRegionDataItem) = viewModelScope.launch(IO) {
        repository.delete(favLocation)
    }

}