package com.example.vculp.features.authentication.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import java.lang.Exception

class SignupViewModel: ViewModel() {
    companion object {
        private val viewModel by lazy { SignupViewModel() }
        fun getInstance() = viewModel
    }

    val userPhoneNumber =  MutableLiveData<String>()
    val userFirstName = MutableLiveData<String>()
    val userLastName = MutableLiveData<String>()

    // TODO: implement network call for otp request and save user data
    fun sendRequest() {
        if(validate(userPhoneNumber.value!!)){
            println("make a request to get the otp from $userPhoneNumber")
        } else {
            throw Exception("invalid phone number")
        }
    }

    // TODO: implement phone no. and userNames validation logic
    private fun validate(userPhoneNumber: String) :Boolean {
        // regular expression for a valid Indian phone number
        val phoneNumberRegex = Regex("^[0-9]{10}$")

        // phone number matches the regular expression
        return userPhoneNumber.matches(phoneNumberRegex)
    }
}