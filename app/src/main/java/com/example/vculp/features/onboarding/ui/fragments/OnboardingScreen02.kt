package com.example.vculp.features.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.vculp.R
import com.example.vculp.databinding.FragmentOnboardingScreen01Binding
import com.example.vculp.databinding.FragmentOnboardingScreen02Binding
import com.example.vculp.shared.data.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OnboardingScreen02 : Fragment() {

    private lateinit var binding: FragmentOnboardingScreen02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding_screen02, container, false)
        binding = FragmentOnboardingScreen02Binding.bind(view)

        binding.button.setOnClickListener {
            finishOnboarding()
        }
        return binding.root
    }


    private fun finishOnboarding() {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigate(R.id.action_onboardingScreenContainer_to_riderFragment)
            DataStoreManager(requireContext()).finishOnboarding()
        }
    }
}