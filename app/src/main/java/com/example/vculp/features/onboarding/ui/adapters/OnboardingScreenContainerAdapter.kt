package com.example.vculp.features.onboarding.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingScreenContainerAdapter(private val fragmentsList: ArrayList<Fragment>, fm: FragmentManager, lifecycle:Lifecycle): FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }

}