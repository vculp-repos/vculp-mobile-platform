package com.example.vculp.features.chooseLocation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.data.models.FavRegionDataItem

class AddFavLocationViewModel: ViewModel() {
    companion object {
        private val viewModel by lazy { AddFavLocationViewModel() }
        fun getInstance() = viewModel
    }
    val toUpdate = MutableLiveData<Boolean>(false)
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val location = MutableLiveData<String>()
}
