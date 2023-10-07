package com.example.vculp.features.bookRide.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.shared.data.models.DriverApiDataItem

class DriverListViewModel: ViewModel() {
    private val _selectedItem = MutableLiveData<DriverApiDataItem?>()
    init {
        _selectedItem.value = null
    }

    val selectedItem: LiveData<DriverApiDataItem?>
        get() = _selectedItem

    fun setSelectedItem(item: DriverApiDataItem){
        _selectedItem.value=item
    }
}