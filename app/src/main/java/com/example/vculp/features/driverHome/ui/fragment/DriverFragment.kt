package com.example.vculp.features.driverHome.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vculp.R
import com.example.vculp.features.driverHome.ui.viewmodels.DriverViewModel

class DriverFragment : Fragment() {

    companion object {
        fun newInstance() = DriverFragment()
    }

    private lateinit var viewModel: DriverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driver, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        // TODO: Use the ViewModel
    }

}