package com.example.vculp.features.authentication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentLoginOtpBinding
import com.example.vculp.features.authentication.ui.viewmodels.LoginViewModel


class LoginOtp : Fragment() {

    private lateinit var binding: FragmentLoginOtpBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginViewModel = LoginViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_otp, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginOtp_to_loginHome)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginOtp_to_riderFragment)
        }
    }


}