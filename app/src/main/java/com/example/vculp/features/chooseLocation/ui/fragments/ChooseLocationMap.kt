package com.example.vculp.features.chooseLocation.ui.fragments

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.vculp.R
import com.example.vculp.features.chooseLocation.ui.viewmodels.AddFavLocationViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.IOException

class ChooseLocationMap : Fragment() {

    private lateinit var userLocationViewModel: UserLocationViewModel
    private lateinit var addFavLocationViewModel: AddFavLocationViewModel
    private lateinit var mMap: GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.style_json))
        val location = userLocationViewModel.currentLocation.value
        if(location != null){
            val locationLatLng = LatLng(location.latitude,location.longitude)
            googleMap.addMarker(MarkerOptions().position(locationLatLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,12f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userLocationViewModel = UserLocationViewModel.getInstance()
        addFavLocationViewModel = AddFavLocationViewModel.getInstance()
        return inflater.inflate(R.layout.fragment_choose_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        updateMap()
    }

    private fun updateMap() {
        addFavLocationViewModel.location.observe(viewLifecycleOwner){
            lifecycleScope.launch(Dispatchers.IO) {
                var location: LatLng?
                location = getCoords(it,requireContext())
                withContext(Dispatchers.Main) {
                    if (location != null) {
                        val locationLatLng = LatLng(location.latitude, location.longitude)
                        mMap.clear()
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 12f))
                        mMap.addMarker(MarkerOptions().position(locationLatLng))
                    }
                }
            }
        }
    }

    fun getCoords(address:String, context: Context): LatLng? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[0]
                val latitude: Double = address.latitude
                val longitude: Double = address.longitude
                return LatLng(latitude, longitude)
            } else {
                Log.d("FragmentViewModel", "No address found for $address")
            }
        } catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }
}