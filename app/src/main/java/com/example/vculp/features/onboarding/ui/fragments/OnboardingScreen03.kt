package com.example.vculp.features.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.vculp.R
import com.example.vculp.databinding.FragmentOnboardingScreen01Binding
import com.example.vculp.databinding.FragmentOnboardingScreen03Binding

class OnboardingScreen03 : Fragment() {

    private lateinit var binding: FragmentOnboardingScreen03Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_onboarding_screen_01, container, false)
        binding = FragmentOnboardingScreen03Binding.bind(view)

        binding.button.setOnClickListener {
            finishOnboarding()
        }
        return binding.root
    }

    private fun finishOnboarding() {

    }

}