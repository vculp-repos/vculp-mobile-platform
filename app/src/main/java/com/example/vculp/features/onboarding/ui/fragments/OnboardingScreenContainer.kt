package com.example.vculp.features.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vculp.R
import com.example.vculp.databinding.FragmentOnboardingScreenContainerBinding
import com.example.vculp.features.onboarding.ui.adapters.OnboardingScreenContainerAdapter


class OnboardingScreenContainer : Fragment() {

    private lateinit var binding: FragmentOnboardingScreenContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding_screen_container, container, false)

        binding = FragmentOnboardingScreenContainerBinding.bind(view)

        val fragmentList = arrayListOf<Fragment>(
            OnboardingScreen01(),
            OnboardingScreen02()
        )

        val adapter = OnboardingScreenContainerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager2.adapter = adapter

        return binding.root

    }



}