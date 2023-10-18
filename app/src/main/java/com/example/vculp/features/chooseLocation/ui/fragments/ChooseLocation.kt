package com.example.vculp.features.chooseLocation.ui.fragments

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FragmentChooseLocationBinding
import com.example.vculp.features.chooseLocation.ui.adapters.LocationRecommendationsAdapter
import com.example.vculp.features.chooseLocation.ui.adapters.OnItemClickListener
import com.example.vculp.features.chooseLocation.ui.adapters.PlacesAutoCompleteListAdapter
import com.example.vculp.features.chooseLocation.ui.viewmodels.AddFavLocationViewModel
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChooseLocation : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentChooseLocationBinding
    private lateinit var locationList: ArrayList<String>
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var placesAutoCompleteList: RecyclerView
    private val addFavLocationViewModel = AddFavLocationViewModel.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // to get the api key
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

        val enterAnimation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.explode
        )

        val exitAnimation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.no_transition
        )

        sharedElementEnterTransition = enterAnimation
        sharedElementReturnTransition = exitAnimation

        // Inflate the layout for this fragment
        binding = FragmentChooseLocationBinding.inflate(inflater, container, false)
        riderViewModel = RiderViewModel.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etStartLocation.text =
            Editable.Factory().newEditable(riderViewModel.currentLocation.value)
        riderViewModel.dropLocation.observe(viewLifecycleOwner) {
            binding.etDropLocation.text = Editable.Factory().newEditable(it)
        }

        locationList = arrayListOf("new delhi", "almora")

        val recommendationsList: RecyclerView = binding.locationRecommendationRecyclerView
        recommendationsList.layoutManager = LinearLayoutManager(context)
        recommendationsList.adapter = LocationRecommendationsAdapter(locationList, this)


        placesAutoCompleteList = binding.placesAutoCompleteList
        placesAutoCompleteList.layoutManager = LinearLayoutManager(context)
        placesAutoCompleteList.adapter = PlacesAutoCompleteListAdapter(listOf(), this ,false)

        binding.findRideBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseLocation_to_chooseRideType)
        }

        binding.etDropLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (binding.placesAutoCompleteList.visibility != View.VISIBLE) {
                    binding.placesAutoCompleteList.visibility = View.VISIBLE
                }
                if (binding.etDropLocation.text.isNotEmpty() && binding.etDropLocation.text.length >= 3) {
                    getPlacesSuggestions(s.toString())
                }else{
                    binding.placesAutoCompleteList.visibility = View.GONE
                }
            }
        })

    }

    override fun onItemClick(position: Int, item: String) {
        println("$item is clicked!!")
        riderViewModel.setDropLocation(item)
        binding.placesAutoCompleteList.visibility = View.GONE
    }

    override fun onSaveBtnClick(position: Int, item: String) {
        addFavLocationViewModel.location.value = item
        findNavController().navigate(R.id.action_chooseLocation_to_addFavLocation)
        riderViewModel.setDropLocation(item)
    }

    private fun getPlacesSuggestions(query: String) {
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
}