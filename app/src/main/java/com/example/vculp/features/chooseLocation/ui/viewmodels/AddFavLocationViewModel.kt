package com.example.vculp.features.chooseLocation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel

class AddFavLocationViewModel: ViewModel() {
    companion object {
        private val viewModel by lazy { AddFavLocationViewModel() }
        fun getInstance() = viewModel
    }

    val location = MutableLiveData<String>()
}