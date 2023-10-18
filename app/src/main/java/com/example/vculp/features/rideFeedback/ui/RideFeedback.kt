package com.example.vculp.features.rideFeedback.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentRideFeedbackBinding
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel

class RideFeedback : Fragment() {


    private lateinit var viewModel: RideFeedbackViewModel
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var binding: FragmentRideFeedbackBinding
    private lateinit var bottomSheet: RideFeedbackBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ride_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRideFeedbackBinding.bind(view)
        riderViewModel = RiderViewModel.getInstance()
        viewModel = ViewModelProvider(this).get(RideFeedbackViewModel::class.java)
        bottomSheet = RideFeedbackBottomSheet()

        riderViewModel.startLocation.observe(viewLifecycleOwner){
            binding.startingLocation.text =  Editable.Factory().newEditable(it)
        }

        riderViewModel.dropLocation.observe(viewLifecycleOwner){
            binding.dropLocation.text = Editable.Factory().newEditable(it)
        }

        binding.paymentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_rideFeedback_to_riderFragment)
        }

        binding.reviewBtn.setOnClickListener {
            bottomSheet.show(requireActivity().supportFragmentManager,RideFeedbackBottomSheet.TAG)
        }
    }
}