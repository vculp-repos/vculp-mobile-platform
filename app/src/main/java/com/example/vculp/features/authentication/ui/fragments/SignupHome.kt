package com.example.vculp.features.authentication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentSignupHomeBinding
import com.example.vculp.features.authentication.ui.viewmodels.SignupViewModel
import java.lang.Exception
import kotlin.math.sign


class SignupHome : Fragment() {

    private lateinit var binding: FragmentSignupHomeBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signupViewModel = SignupViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_home, container, false)
        binding.signupViewModel = signupViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoginBtn.setOnClickListener{
            findNavController().navigate(R.id.action_signupHome_to_loginHome)
        }

        binding.btnSignUp.setOnClickListener {
            handleSubmitBtnClick()
        }
    }


    private fun handleSubmitBtnClick(){
        if(signupViewModel.userPhoneNumber.value == null || signupViewModel.userFirstName.value == null) {
            Toast.makeText(context,"please enter all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            signupViewModel.sendRequest()
        } catch(e: Exception) {
            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            return
        }

//        navigate to login otp screen
        findNavController().navigate(R.id.action_signupHome_to_signupOtp)
    }

}