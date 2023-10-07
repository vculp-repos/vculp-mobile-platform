package com.example.vculp.features.authentication.ui.viewmodels

import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import java.lang.Exception

class LoginViewModel: ViewModel() {

    companion object {
        private val viewModel by lazy { LoginViewModel() }
        fun getInstance() = viewModel
    }

    val userPhoneNumber =  MutableLiveData<String>()

    // TODO: implement network call for otp request
    fun sendRequest() {
        if(validate(userPhoneNumber.value!!)){
            println("make a request to get the otp from ${userPhoneNumber.value}")
        } else {
            throw Exception("the phone number is invalid")
        }
    }

    // TODO: implement phone no. validation logic 
    private fun validate(userPhoneNumber: String) :Boolean {
        // regular expression for a valid Indian phone number
        val phoneNumberRegex = Regex("^[0-9]{10}$")

        // phone number matches the regular expression
        return userPhoneNumber.matches(phoneNumberRegex)
    }
}