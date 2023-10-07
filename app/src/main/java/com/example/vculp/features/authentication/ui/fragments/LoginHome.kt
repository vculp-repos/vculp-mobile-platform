package com.example.vculp.features.authentication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentLoginHomeBinding
import com.example.vculp.features.authentication.ui.viewmodels.LoginViewModel
import java.lang.Exception


class LoginHome : Fragment() {
    private lateinit var binding: FragmentLoginHomeBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginViewModel = LoginViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login_home, container, false)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginHome_to_signupHome)
        }

        binding.btnLogin.setOnClickListener {
            handleSubmitBtnClick()
        }
    }

    private fun handleSubmitBtnClick(){
        if(loginViewModel.userPhoneNumber.value == null) {
            Toast.makeText(context,"please enter the phone number", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            loginViewModel.sendRequest()
        } catch(e: Exception) {
            Toast.makeText(context,"the phone number is invalid", Toast.LENGTH_SHORT).show()
            return
        }

//        navigate to login otp screen
        findNavController().navigate(R.id.action_loginHome_to_loginOtp)
    }

}

