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
import com.example.vculp.features.bookRide.ui.viewmodels.ChooseRideTypeViewModelFactory
import com.example.vculp.features.bookRide.ui.viewmodels.RideOptions
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.data.models.FareRecommendationData

class ChooseRideType : Fragment() {



    private lateinit var chooseRideTypeViewModel: ChooseRideTypeViewModel
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var binding: FragmentChooseRideTypeBinding
    private var currentlySelectedBtn: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        riderViewModel = RiderViewModel.getInstance()
        val factory = ChooseRideTypeViewModelFactory(riderViewModel)
        chooseRideTypeViewModel = ViewModelProvider(this,factory)[ChooseRideTypeViewModel::class.java]
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
        chooseRideTypeViewModel.setSelectedRide("bike", binding.bikeBtn)

        chooseRideTypeViewModel.selectedBtnView.observe(viewLifecycleOwner) {
            if(currentlySelectedBtn != null) {
                currentlySelectedBtn?.setBackgroundResource(0)
            }
            it.setBackgroundResource(R.drawable.selected_btn_background_choose_ride_list)
            currentlySelectedBtn = it
        }

        chooseRideTypeViewModel.selectedRide.observe(viewLifecycleOwner){
            if(it!=null) {
                showSelectedRideData(it)
            }
        }

        showRideOptions(chooseRideTypeViewModel.rideOptions.value)

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

    private fun showSelectedRideData(selectedRide: FareRecommendationData){

        binding.etSelectedRidePrice.text = Editable.Factory().newEditable(selectedRide.value[0].recommendedDistanceFare.toString())
        binding.tvSelectedRideETA.text = selectedRide.value[0].duration.toString()

        when(selectedRide.value[0].vehicleTypeId) {
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

    private fun showRideOptions(rideOptions: ArrayList<FareRecommendationData>?){
        rideOptions?.forEach {
            var option = it.value[0]
            when(option.vehicleTypeId) {
                "auto" -> {
                    binding.tvAutoFare.text = "Rs. ${option.baseFare}"
                    binding.tvAutoArrivalTime.text = option.duration.toString()
                }
                "bike" -> {
                    binding.tvBikeFare.text = "Rs. ${option.baseFare}"
                    binding.tvBikeArrivalTime.text = option.duration.toString()
                }
                "mini" -> {
                    binding.tvMiniFare.text = "Rs. ${option.baseFare}"
                    binding.tvMiniArrivalTime.text = option.duration.toString()
                }
                "sedan" -> {
                    binding.tvSedanFare.text = "Rs. ${option.baseFare}"
                    binding.tvSedanArrivalTime.text = option.duration.toString()
                }
                "xl" -> {
                    binding.tvXlFare.text = "Rs. ${option.baseFare}"
                    binding.tvXlArrivalTime.text = option.duration.toString()
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