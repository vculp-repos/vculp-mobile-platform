package com.example.vculp.features.riderHome.ui.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FragmentRiderBinding
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.features.chooseLocation.ui.adapters.OnItemClickListener
import com.example.vculp.features.chooseLocation.ui.adapters.PlacesAutoCompleteListAdapter
import com.example.vculp.features.chooseLocation.ui.fragments.ChooseLocation
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.data.AppDatabase
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModelFactory
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import com.example.vculp.utils.CustomLocationManager
import com.example.vculp.utils.PermissionsHandler
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.Exception


class RiderFragment : Fragment(), OnItemClickListener {
    private lateinit var viewModel: RiderViewModel
    private lateinit var binding: FragmentRiderBinding
    private lateinit var userLocationViewModel: UserLocationViewModel
    private lateinit var favLocationViewModel: FavLocationsViewModel
    private lateinit var locationManager: CustomLocationManager
    private lateinit var mapView: MapView
    private var permissionsHandler = PermissionsHandler()
    private var activeCurrentLocationEtCheck: Boolean = false
    private lateinit var placesAutoCompleteList: RecyclerView

    val CurrentRide = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = RiderViewModel.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rider, container, false)
        binding.viewModel = viewModel
        userLocationViewModel = UserLocationViewModel.getInstance()
        binding.lifecycleOwner = this

//        for the map
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        setUpMap(null)

//        request location permissions
        locationManager = CustomLocationManager(requireContext())
        if (!locationManager.checkLocationPermissions()) permissionsHandler.requestLocationPermissions(
            requireActivity()
        )

        val dao = AppDatabase.getInstance(requireContext().applicationContext).favLocationsDao
        val repository = FavLocationsRepository(dao)
        val factory = FavLocationsViewModelFactory(repository)
        favLocationViewModel = ViewModelProvider(this, factory)[FavLocationsViewModel::class.java]



        return binding.root
    }

    private fun handleQuickRideBtns(itemName: String, location: FavLocation?) {
        when (itemName) {
            "home" -> binding.homeBtn.visibility = if(location != null) View.VISIBLE else View.GONE
            "office" -> binding.officeBtn.visibility = if(location != null) View.VISIBLE else View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesAutoCompleteList = binding.currentLocationSuggestionsRv
        placesAutoCompleteList.layoutManager = LinearLayoutManager(context)
        placesAutoCompleteList.adapter = PlacesAutoCompleteListAdapter(listOf(), this ,true)

        userLocationViewModel.currentLocation.observe(viewLifecycleOwner) {
            val tempCoords = LatLng(it.latitude, it.longitude)
            Log.i("location", "onActivityCreated: $tempCoords")
            binding.etCurrentLocation.text = Editable.Factory()
                .newEditable(viewModel.locationFromCords(tempCoords, requireContext())!!)
        }

//        updateGreeting(binding.tvGreeting)

        updateCurrentLocation()

        favLocationViewModel.home.observe(viewLifecycleOwner) {
            handleQuickRideBtns("home",it)
        }

        favLocationViewModel.office.observe(viewLifecycleOwner) {
            handleQuickRideBtns("office", it)
        }

        favLocationViewModel.favLocations.observe(viewLifecycleOwner){
            binding.favLocationsBtn.visibility = if(it.isEmpty()) { View.GONE } else View.VISIBLE
            binding.addFavLocationBtn.visibility = if(it.isNotEmpty()) { View.GONE } else View.VISIBLE
        }

        binding.tvDropLocation.setOnClickListener {
            navigateToNextScreen()
        }

        binding.addFavLocationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_riderFragment_to_addFavLocation)
        }

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_riderFragment_to_chooseLocation)
        }

        binding.officeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_riderFragment_to_chooseLocation)
        }

        binding.favLocationsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_riderFragment_to_favLocations)
        }

        binding.currentLocationBtn.setOnClickListener {
            println("current location update btn clicked!!!")
            updateCurrentLocation()
        }

        binding.etCurrentLocation.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                activeCurrentLocationEtCheck = true
            }
            false
        }

        binding.etCurrentLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!activeCurrentLocationEtCheck) return
                if (binding.currentLocationSuggestionsRv.visibility != View.VISIBLE) {
                    binding.currentLocationSuggestionsRv.visibility = View.VISIBLE
                }
                if (binding.etCurrentLocation.text.isNotEmpty() && binding.etCurrentLocation.text.length >= 2) {
                    getPlacesSuggestions(s.toString())
                }else{
                    binding.currentLocationSuggestionsRv.visibility = View.GONE
                }
            }
        })
    }

    private fun getPlacesSuggestions(query: String) {
        val applicationContext = requireContext().applicationContext
        val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val value = ai.metaData.get("com.google.android.geo.API_KEY")
        val apiKey = value.toString()

        // initializing Places sdk
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        var favLocationsList = ArrayList<String>()
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountries("IN").setSessionToken(token)
            .setQuery(query).build()
        val placesClient = Places.createClient(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    favLocationsList.clear()
                    favLocationsList.addAll(response.autocompletePredictions.map {
                        it.getFullText(null).toString()
                    })
                    updateLocationSuggestionList(favLocationsList)
                }.addOnFailureListener { exception: Exception? ->
                    if (exception is ApiException) {
                        Log.e("location", "Place not found: ${exception.statusCode}")
                    }
                }
        }
    }

    private fun updateLocationSuggestionList(newList: List<String>) {
        (placesAutoCompleteList.adapter as? PlacesAutoCompleteListAdapter)?.updateData(newList)
    }

    private fun setLocationFromSuggestions(cl: String){
        viewModel.setStartLocation(cl)
        binding.currentLocationSuggestionsRv.visibility = View.GONE
        activeCurrentLocationEtCheck = false
    }

    private fun setUpMap(usersLastKnownLocation: Location?) {
        binding.mapView.getMapAsync { googleMap ->
            googleMap.setMinZoomPreference(5f)
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.style_json
                )
            )
            if (usersLastKnownLocation != null) {
                val userCords =
                    LatLng(usersLastKnownLocation.latitude, usersLastKnownLocation.longitude)
                // resizing the bitmapIcon
                val currentLocationMarkerIcon: Bitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.current_location
                    ), 80, 80, false
                )

                // showing information about that place.
                googleMap.addMarker(
                    MarkerOptions()
                        .title("current location")
                        .icon(BitmapDescriptorFactory.fromBitmap(currentLocationMarkerIcon))
                        .position(userCords)
                        .snippet("current location")
                )!!

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        userCords, 12f
                    )
                )
            }
        }
    }

    private fun navigateToOnboardingScreen(){

    }

    private fun navigateToNextScreen() {
        val extras = FragmentNavigatorExtras(
            binding.tvDropLocation to "etDropChooseLocation",
            binding.etCurrentLocation to "etStartChooseLocation"
        )
        findNavController().navigate(
            R.id.action_riderFragment_to_chooseLocation,
            null,
            null,
            extras
        )
    }

    private fun updateGreeting(textView: TextView) {
        TODO("implement the function later")
    }

    private fun updateCurrentLocation() {
        try {
            val usersLastKnownLocation = locationManager.getLastKnownLocation(requireActivity())

            if (locationManager.checkLocationPermissions()) {
                usersLastKnownLocation.addOnCompleteListener {
                    if (it.result != null) {
                        userLocationViewModel.updateLocation(it.result)
                        setUpMap(it.result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("location", "onViewCreated: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onItemClick(position: Int, item: String) {
        setLocationFromSuggestions(item)
    }

    override fun onSaveBtnClick(position: Int, item: String) {}
}