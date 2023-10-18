package com.example.vculp.features.currentRide

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vculp.R
import com.example.vculp.databinding.FragmentLiveRideBinding
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.utils.LocationTrackingService


class LiveRideFragment : Fragment() {
    private lateinit var binding: FragmentLiveRideBinding
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var userLocationViewModel: UserLocationViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        riderViewModel = RiderViewModel.getInstance()
        userLocationViewModel = UserLocationViewModel.getInstance()
        return inflater.inflate(R.layout.fragment_live_ride, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLiveRideBinding.bind(view)

        riderViewModel.dropLocation.observe(viewLifecycleOwner){
            if(riderViewModel.coords.value != null) binding.destinationName.text = it
        }

        riderViewModel.duration.observe(viewLifecycleOwner){
            binding.durationLeft.text = it.text
        }

        binding.changeDestinationBtn.setOnClickListener {
            if(!binding.etChangeDestination.isVisible) binding.etChangeDestination.visibility = View.VISIBLE
            else {
                val coords  = riderViewModel.getCoords(binding.etChangeDestination.text.toString(),requireContext())
                if(coords!=null){
                    riderViewModel.dropLocation.value = binding.etChangeDestination.text.toString()
                    binding.etChangeDestination.visibility = View.GONE
                }else {
                    Toast.makeText(requireContext(),"try different locaiton",Toast.LENGTH_SHORT).show()
                    binding.etChangeDestination.text.clear()
                }
            }
        }

        binding.sosBtn.setOnClickListener {
            val sosNumber = "101010"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$sosNumber")
            if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"calling permission required for this feature",Toast.LENGTH_SHORT).show()
            }else {
                startActivity(intent)
            }
        }

        binding.endRideBtn.setOnClickListener {
            Intent(requireContext().applicationContext, LocationTrackingService::class.java).also {
                it.action = LocationTrackingService.Actions.STOP.toString()
                requireActivity().stopService(it)
            }
            findNavController().navigate(R.id.action_liveRideFragment_to_rideFeedback)
        }

    }


}