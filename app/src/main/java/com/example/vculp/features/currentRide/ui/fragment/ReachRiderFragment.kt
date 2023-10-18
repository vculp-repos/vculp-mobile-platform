package com.example.vculp.features.currentRide.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentReachRiderBinding
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import kotlinx.coroutines.launch

class ReachRiderFragment: Fragment() {

    private lateinit var binding: FragmentReachRiderBinding
    private lateinit var viewModel: RiderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = RiderViewModel.getInstance()
        return inflater.inflate(R.layout.fragment_reach_rider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReachRiderBinding.bind(view)
        binding.cardView.setBackgroundResource(R.drawable.top_card_bg)
        lifecycleScope.launch {
            viewModel.startLocation.observe(viewLifecycleOwner) {
                binding.currentLocation.text = Editable.Factory.getInstance().newEditable(it)
            }

            viewModel.dropLocation.observe(viewLifecycleOwner) {
                binding.dropLocation.text = Editable.Factory.getInstance().newEditable(it)
            }
        }

        viewModel.duration.observe(viewLifecycleOwner){
            binding.durationTitle.text = "Ride arriving in ${it.text}"
        }

        binding.startRide.setOnClickListener {
            findNavController().navigate(R.id.action_reachRiderFragment_to_liveRideFragment)
        }
    }

}