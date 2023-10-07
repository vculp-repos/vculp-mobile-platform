package com.example.vculp.shared.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.features.chooseLocation.ui.fragments.FavLocations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavLocationsViewModel (private val repository: FavLocationsRepository) : ViewModel() {

    val favLocations = repository.favLocations
    val home = repository.home
    val office = repository.office
    val selectedLocation = MutableLiveData<FavLocation>()

    fun setSelectedLocation(location: FavLocation){
        selectedLocation.postValue(location)
    }

    fun insert(favLocation: FavLocation) = viewModelScope.launch(IO) {
        repository.insert(favLocation)
    }

    fun update(favLocation: FavLocation) = viewModelScope.launch(IO) {
        repository.update(favLocation)
    }

    fun delete(favLocation: FavLocation) = viewModelScope.launch(IO) {
        repository.delete(favLocation)
    }

}