package com.example.vculp.features.bookRide.ui.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface RideData {
    val rideType: String
    val arrivalTime: String // convert it into time
    val rideFare: String
}

class RideDataImpl(
    override val rideType: String,
    override val arrivalTime: String,
    override val rideFare: String
) : RideData {
}

data class RideOptions (
    val newPrice: String,
    val acOption: Boolean
)

class ChooseRideTypeViewModel : ViewModel() {

    val selectedRide = MutableLiveData<RideData>()
    val riderRideFare = MutableLiveData<String>()
    val selectedBtnView = MutableLiveData<View>()


    val dummyRideOptions: Array<RideData> = arrayOf(
        RideDataImpl("bike","2 min","100"),
        RideDataImpl("auto","2 min","120"),
        RideDataImpl("mini","4 min","190"),
        RideDataImpl("sedan","6 min","110"),
        RideDataImpl("xl","4 min","300")
    )

    init {
        selectedRide.value = dummyRideOptions[0]
    }

    fun updateRideFare(amount: Int) {
        var tempFare = riderRideFare.value?.toInt()
        tempFare = tempFare?.plus(amount)
        riderRideFare.value = tempFare.toString()
    }


    fun setSelectedRide(selectedRideType: String, selectedBtnView: View) {
//        riderRideFare.postValue(selectedRide.rideFare)
        val selectedRide = dummyRideOptions.find { it.rideType == selectedRideType}
        this.selectedRide.postValue(selectedRide)
        this.selectedBtnView.postValue(selectedBtnView)
    }

    fun setSelectedBtnView(selectedBtnView: View){
        this.selectedBtnView.postValue(selectedBtnView)
    }

    fun handleRequestRideEvent(rideOptions: RideOptions): Boolean{
        Log.i("RequestRide","you requested ${selectedRide.value?.rideType} for Rs ${rideOptions.newPrice}")

        return true
    }

}