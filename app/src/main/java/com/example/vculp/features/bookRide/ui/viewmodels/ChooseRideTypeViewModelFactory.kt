package com.example.vculp.features.bookRide.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel

class ChooseRideTypeViewModelFactory(private val riderViewModel: RiderViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChooseRideTypeViewModel::class.java)){
            return ChooseRideTypeViewModel(riderViewModel) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }
}