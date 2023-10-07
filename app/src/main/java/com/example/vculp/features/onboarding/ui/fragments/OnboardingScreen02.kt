package com.example.vculp.features.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.vculp.R
import com.example.vculp.databinding.FragmentOnboardingScreen01Binding
import com.example.vculp.databinding.FragmentOnboardingScreen02Binding


class OnboardingScreen02 : Fragment() {

    private lateinit var binding: FragmentOnboardingScreen02Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_onboarding_screen02, container, false)
        binding = FragmentOnboardingScreen02Binding.bind(view)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager2)
        binding.button.setOnClickListener {
            viewPager?.currentItem = 2
        }
        return binding.root
    }


}