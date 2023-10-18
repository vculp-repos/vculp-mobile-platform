package com.example.vculp.features.bookRide.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.shared.data.models.DriverApiData
import com.example.vculp.shared.data.models.DriverApiDataItem
import com.example.vculp.databinding.FragmentDriversListBinding
import com.example.vculp.features.bookRide.ui.adapter.DriversListAdapter
import com.example.vculp.network.RetrofitBuilder
import com.example.vculp.network.UserLocationImpl
import com.example.vculp.features.bookRide.ui.viewmodels.DriverListViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DriversListFragment : Fragment() {

    private lateinit var binding: FragmentDriversListBinding
    private  var driversDataArray = DriverApiData()
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var driversListViewModel: DriverListViewModel
    private lateinit var bottomSheetNegotiateFragment: BottomSheetNegotiateFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drivers_list, container, false)
        binding.lifecycleOwner = this
        riderViewModel = RiderViewModel.getInstance()
        driversListViewModel = ViewModelProvider(this).get(DriverListViewModel::class.java)

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // populate the start and end location
        binding.apply {
            tvStartLocation.text = riderViewModel.startLocation.value
            tvEndLocation.text = riderViewModel.dropLocation.value
        }

        bottomSheetNegotiateFragment = BottomSheetNegotiateFragment()

        val driversList: RecyclerView = binding.recyclerView
        driversList.layoutManager = LinearLayoutManager(context)
        driversList.adapter = DriversListAdapter(driversDataArray,
            { position:Int, item:DriverApiDataItem -> onNegotiateClick(position,item) },{ position:Int, item:DriverApiDataItem -> onSaveBtnClick(position,item) })


        val userLocationHelper = UserLocationImpl(RetrofitBuilder.userLocationService)


        binding.cancelRideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driversListFragment_to_riderFragment)
        }


        lifecycleScope.launch(Dispatchers.IO) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
            driversDataArray = userLocationHelper.getDriversList(riderViewModel.startLocation.value!!,riderViewModel.dropLocation.value!!)
            Log.i("drivers_list", "onViewCreated: $driversDataArray")


            withContext(Dispatchers.Main){
                if(driversDataArray.isNotEmpty()){
                    (driversList.adapter as? DriversListAdapter)?.updateData(driversDataArray)
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun onNegotiateClick(position: Int, item: DriverApiDataItem) {
        bottomSheetNegotiateFragment.show(requireActivity().supportFragmentManager,"BottomSheetNegotiateFragment")
    }

    private fun onSaveBtnClick(position:Int, item:DriverApiDataItem){
        driversListViewModel.setSelectedItem(item)
        findNavController().navigate(R.id.action_driversListFragment_to_reachRiderFragment)
    }

}