package com.example.vculp.features.bookRide.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentChooseRideTypeBinding
import com.example.vculp.features.bookRide.ui.viewmodels.ChooseRideTypeViewModel
import com.example.vculp.features.bookRide.ui.viewmodels.RideOptions
import com.example.vculp.features.bookRide.ui.viewmodels.RideData
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel

class ChooseRideType : Fragment() {



    private lateinit var chooseRideTypeViewModel: ChooseRideTypeViewModel
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var binding: FragmentChooseRideTypeBinding
    private var currentlySelectedBtn: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chooseRideTypeViewModel = ViewModelProvider(this).get(ChooseRideTypeViewModel::class.java)
        riderViewModel = RiderViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_ride_type, container, false)
        binding.chooseRideTypeViewModel = chooseRideTypeViewModel
        binding.riderViewModel = riderViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        setClickListenerToHorizontalList()

        riderViewModel.startLocation.observe(viewLifecycleOwner){
            binding.startingLocation.text = it
        }

        riderViewModel.dropLocation.observe(viewLifecycleOwner){
            binding.dropLocation.text = it
        }

        chooseRideTypeViewModel.setSelectedBtnView(binding.bikeBtn)

        chooseRideTypeViewModel.selectedBtnView.observe(viewLifecycleOwner) {
            if(currentlySelectedBtn != null) {
                currentlySelectedBtn?.setBackgroundResource(0)
            }
            it.setBackgroundResource(R.drawable.selected_btn_background_choose_ride_list)
            currentlySelectedBtn = it
        }

        chooseRideTypeViewModel.selectedRide.observe(viewLifecycleOwner){
            showSelectedRideData(it)
        }

        showRideOptions(chooseRideTypeViewModel.dummyRideOptions)

        binding.increaseRideFareBtn.setOnClickListener {
            chooseRideTypeViewModel.updateRideFare(10)
        }

        binding.decreaseFareBtn.setOnClickListener {
            chooseRideTypeViewModel.updateRideFare(-10)
        }

        binding.requestRideBtn.setOnClickListener {
            handleRequestBtnClick()
        }
    }

    private fun setClickListenerToHorizontalList(){
        binding.autoBtn.setOnClickListener {
            chooseRideTypeViewModel.setSelectedRide("auto",it)
        }
        binding.bikeBtn.setOnClickListener {
            chooseRideTypeViewModel.setSelectedRide("bike", it)
        }
        binding.miniBtn.setOnClickListener {
            chooseRideTypeViewModel.setSelectedRide("mini", it)
        }
        binding.sedanBtn.setOnClickListener {
            chooseRideTypeViewModel.setSelectedRide("sedan", it)
        }
        binding.xlBtn.setOnClickListener {
            chooseRideTypeViewModel.setSelectedRide("xl", it)
        }
    }

    private fun showSelectedRideData(selectedRide: RideData){

        binding.etSelectedRidePrice.text = Editable.Factory().newEditable(selectedRide.rideFare)
        binding.tvSelectedRideETA.text = selectedRide.arrivalTime

        when(selectedRide.rideType) {
            "auto" -> {
                binding.selectedRideImageView.setImageResource(R.drawable.auto_icon)
            }
            "bike" -> {
                binding.selectedRideImageView.setImageResource(R.drawable.bike_icon)
            }
            "mini" -> {
                binding.selectedRideImageView.setImageResource(R.drawable.mini_icon)
            }
            "sedan" -> binding.selectedRideImageView.setImageResource(R.drawable.sedan_icon)
            "xl" -> binding.selectedRideImageView.setImageResource(R.drawable.xl_icon)
            else -> {
                println("don't have their icons")
            }
        }
    }

    private fun showRideOptions(rideOptions: Array<RideData>){
        rideOptions.forEach {
            when(it.rideType) {
                "auto" -> {
                    binding.tvAutoFare.text = "Rs. ${it.rideFare}"
                    binding.tvAutoArrivalTime.text = it.arrivalTime
                }
                "bike" -> {
                    binding.tvBikeFare.text = "Rs. ${it.rideFare}"
                    binding.tvBikeArrivalTime.text = it.arrivalTime
                }
                "mini" -> {
                    binding.tvMiniFare.text = "Rs. ${it.rideFare}"
                    binding.tvMiniArrivalTime.text = it.arrivalTime
                }
                "sedan" -> {
                    binding.tvSedanFare.text = "Rs. ${it.rideFare}"
                    binding.tvSedanArrivalTime.text = it.arrivalTime
                }
                "xl" -> {
                    binding.tvXlFare.text = "Rs. ${it.rideFare}"
                    binding.tvXlArrivalTime.text = it.arrivalTime
                }
            }
        }
    }

    private fun handleRequestBtnClick() {
        val newPrice = binding.etSelectedRidePrice.text.toString()
        val acOptionChecked = binding.acOptionBtn.isChecked
        val requestSucceed = chooseRideTypeViewModel.handleRequestRideEvent(RideOptions(newPrice,acOptionChecked))
        if (requestSucceed) findNavController().navigate(R.id.action_chooseRideType_to_driversListFragment)
    }

}