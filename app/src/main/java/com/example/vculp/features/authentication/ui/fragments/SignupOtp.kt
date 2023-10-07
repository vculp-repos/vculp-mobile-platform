package com.example.vculp.features.authentication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentSignupOtpBinding
import com.example.vculp.features.authentication.ui.viewmodels.SignupViewModel


class SignupOtp : Fragment() {

 private lateinit var binding: FragmentSignupOtpBinding
 private lateinit var signupViewModel: SignupViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signupViewModel = SignupViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_otp, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signupOtp_to_signupHome)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signupOtp_to_riderFragment)
        }
    }

}