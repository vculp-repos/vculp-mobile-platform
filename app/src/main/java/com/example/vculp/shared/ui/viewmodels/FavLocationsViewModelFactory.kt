package com.example.vculp.shared.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository

class FavLocationsViewModelFactory(private val repository: FavLocationsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavLocationsViewModel::class.java)){
            return FavLocationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }
}