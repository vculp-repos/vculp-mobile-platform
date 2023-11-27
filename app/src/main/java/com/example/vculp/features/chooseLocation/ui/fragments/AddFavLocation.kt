package com.example.vculp.features.chooseLocation.ui.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FragmentAddFavLocationBinding
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.features.chooseLocation.ui.adapters.FavLocationsListAdapter
import com.example.vculp.features.chooseLocation.ui.adapters.OnItemClickListener
import com.example.vculp.features.chooseLocation.ui.adapters.PlacesAutoCompleteListAdapter
import com.example.vculp.features.chooseLocation.ui.viewmodels.AddFavLocationViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModelFactory
import com.example.vculp.shared.data.AppDatabase
import com.example.vculp.shared.data.models.FavRegionDataItem
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddFavLocation : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentAddFavLocationBinding
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var adapter: FavLocationsListAdapter
    private val favLocationList = ArrayList<FavRegionDataItem>()
    private val addFavLocationViewModel = AddFavLocationViewModel.getInstance()
    private lateinit var placesAutoCompleteList: RecyclerView
    private lateinit var placesClient: PlacesClient
    private lateinit var viewModel: FavLocationsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        riderViewModel = RiderViewModel.getInstance()

        placesClient = Places.createClient(requireContext())

        val dao = AppDatabase.getInstance(requireContext().applicationContext).favLocationsDao
        val repository = FavLocationsRepository(dao)
        val favLocationsViewModel: FavLocationsViewModel by activityViewModels {
            FavLocationsViewModelFactory(repository)
        }
        viewModel = favLocationsViewModel

        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_fav_location, container, false)
        binding.viewModel = viewModel
        binding.addFavLocationViewModel = addFavLocationViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (addFavLocationViewModel.toUpdate.value == true) {
            binding.topAppBar.title = "Update Location Data"
            binding.saveLocationBtn.text = "Update"
        }


        binding.saveLocationBtn.setOnClickListener {
            if (binding.etLocationTitle.text.isNullOrEmpty() || addFavLocationViewModel.location.value.isNullOrEmpty()) return@setOnClickListener
            Log.i(
                "favregion",
                "favregion calling with add fav location ${addFavLocationViewModel.toUpdate.value} ${viewModel.selectedLocation.value}"
            )
            if (addFavLocationViewModel.toUpdate.value == false) {
                val latitude = addFavLocationViewModel.latitude.value
                val longitude = addFavLocationViewModel.longitude.value
                val location = addFavLocationViewModel.location.value.toString()
                val title = binding.etLocationTitle.text.toString()
                if (latitude != null && longitude != null) {

                    viewModel.insert(
                        FavLocation(
                            latitude = latitude,
                            longitude = longitude,
                            title = title,
                            address = location
                        )
                    )
                    findNavController().navigate(R.id.action_addFavLocation_to_chooseLocation)
                }
            }else{
                try{
                    val selectedPlace = viewModel.selectedLocation.value!!
                    selectedPlace.areaName = binding.etSelectedLocation.text.toString()
                    Log.i("favregion", "onViewCreated: $selectedPlace")
                    viewModel.update(selectedPlace)
                    viewModel.getAllLocations()
                    viewModel.setSelectedLocation(null)
                    addFavLocationViewModel.toUpdate.value = false
                    findNavController().navigate(R.id.action_addFavLocation_to_favLocations)
                }catch(e: Exception){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        placesAutoCompleteList = binding.autoCompleteRv
        placesAutoCompleteList.layoutManager = LinearLayoutManager(context)
        placesAutoCompleteList.adapter = PlacesAutoCompleteListAdapter(listOf(), this, true)


        binding.etSelectedLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (placesAutoCompleteList.visibility != View.VISIBLE) {
                    placesAutoCompleteList.visibility = View.VISIBLE
                }
                if (binding.etSelectedLocation.text.isNotEmpty() && binding.etSelectedLocation.isFocused) {
                    getPlacesSuggestions(s.toString())
                } else {
                    placesAutoCompleteList.visibility = View.GONE
                }
            }
        })
    }


    override fun onItemClick(position: Int, item: AutocompleteLocation) {
        binding.autoCompleteRv.visibility = View.GONE
        getPlaceData(item.id).addOnSuccessListener { response ->
            val place = response.place
            addFavLocationViewModel.latitude.value = place.latLng?.latitude
            addFavLocationViewModel.longitude.value = place.latLng?.longitude
            addFavLocationViewModel.location.value = item.address
        }
    }

    override fun onSaveBtnClick(position: Int, item: AutocompleteLocation) {}


    private fun getPlacesSuggestions(query: String) {
        val favLocationsList = ArrayList<AutocompleteLocation>()
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountries("IN").setSessionToken(token)
            .setQuery(query).build()

        lifecycleScope.launch(Dispatchers.IO) {
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    favLocationsList.clear()
                    favLocationsList.addAll(response.autocompletePredictions.map {
                        AutocompleteLocation(it.getFullText(null).toString(), it.placeId)
                    })
                    updateLocationSuggestionList(favLocationsList)
                }.addOnFailureListener { exception: Exception? ->
                    if (exception is ApiException) {
                        Log.e("location", "Place not found: ${exception.statusCode}")
                    }
                }
        }
    }

    private fun getPlaceData(query: String): Task<FetchPlaceResponse> {
        val request =
            FetchPlaceRequest.builder(query, listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG))
                .build()
        return placesClient.fetchPlace(request)
    }

    private fun updateLocationSuggestionList(newList: List<AutocompleteLocation>) {
        (placesAutoCompleteList.adapter as? PlacesAutoCompleteListAdapter)?.updateData(newList)
    }

}