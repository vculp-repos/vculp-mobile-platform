package com.example.vculp.features.bookRide.ui.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.network.NetworkImpl
import com.example.vculp.network.RetrofitBuilder
import com.example.vculp.shared.data.models.FareRecommendationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException




data class RideOptions (
    val newPrice: String,
    val acOption: Boolean
)

class ChooseRideTypeViewModel(private val riderViewModel: RiderViewModel) : ViewModel() {

    val selectedRide = MutableLiveData<FareRecommendationItem>()
    val riderRideFare = MutableLiveData<String>()
    val selectedBtnView = MutableLiveData<View>()
    val rideOptions = MutableLiveData<ArrayList<FareRecommendationItem>?>()

    val options: ArrayList<String> = arrayListOf("auto","bike","mini","sedan","xl")

    init {
        options.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                var tempRideOptions = rideOptions.value
                val rideOption = getFareRecommendation(riderViewModel.startLocation.value!!, riderViewModel.dropLocation.value!!,it,it,2)
                if (tempRideOptions != null && rideOption!= null) {
                    tempRideOptions.add(rideOption)
                }else{
                    if(rideOption!=null){
                        tempRideOptions = arrayListOf(rideOption)
                    }
                }
                rideOptions.postValue(tempRideOptions)
            }
        }
    }


    fun updateRideFare(amount: Int) {
        var tempFare = riderRideFare.value?.toInt()
        tempFare = tempFare?.plus(amount)
        riderRideFare.value = tempFare.toString()
    }


    fun setSelectedRide(selectedRideType: String, selectedBtnView: View) {
//        riderRideFare.postValue(selectedRide.rideFare)
        val origin = riderViewModel.startLocation.value!!
        val destination = riderViewModel.dropLocation.value!!

        viewModelScope.launch(Dispatchers.IO) {
            val selectedRide = getFareRecommendation(origin, destination, selectedRideType, selectedRideType, 2)
            this@ChooseRideTypeViewModel.selectedRide.postValue(selectedRide)
        }
        this.selectedBtnView.postValue(selectedBtnView)
    }

    fun setSelectedBtnView(selectedBtnView: View){
        this.selectedBtnView.postValue(selectedBtnView)
    }

    fun handleRequestRideEvent(rideOptions: RideOptions): Boolean{
        Log.i("RequestRide","you requested ${selectedRide.value?.value?.get(0)?.vehicleTypeId} for Rs ${rideOptions.newPrice}")
        return true
    }

    suspend fun getFareRecommendation(
        origin: String,
        destination: String,
        vehicleType: String,
        vehicleBodyType: String,
        vehicleNoOfSeater: Int
    ): FareRecommendationItem? {
        val networkService = NetworkImpl(RetrofitBuilder.networkService)
        try {
//            return networkService.getFareRecommendation(
//                origin,
//                destination,
//                vehicleType,
//                vehicleBodyType,
//                vehicleNoOfSeater
//            )
            return networkService.getFareRecommendation(
                "delhi",
                "almora",
                "auto",
                "suv",
                5
            )
                // Handle a successful response
        } catch (e: HttpException) {
            // Handle the HTTP error (e.g., 500) here
            // You can access the error response with e.response()
            // Provide user-friendly error messages or log the error details
            Log.i("Fare Recommender", "getFareRecommendation: ${e}")
            return null
        } catch (e: Throwable) {
            // Handle other exceptions (e.g., network issues)
            // Provide appropriate error handling for non-HTTP exceptions
            Log.i("Fare Recommender", "getFareRecommendation: some non http error")
            return null
        }
    }
}