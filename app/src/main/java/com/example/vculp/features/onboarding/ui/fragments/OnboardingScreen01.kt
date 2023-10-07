package com.example.vculp.features.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.vculp.R
import com.example.vculp.databinding.FragmentOnboardingScreen01Binding

class OnboardingScreen01 : Fragment() {

    private lateinit var binding: FragmentOnboardingScreen01Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_onboarding_screen_01, container, false)
        binding = FragmentOnboardingScreen01Binding.bind(view)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager2)
        binding.button.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return binding.root
    }
}