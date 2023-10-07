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
import androidx.lifecycle.ViewModelProvider
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
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModelFactory
import com.example.vculp.shared.data.AppDatabase
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse


class AddFavLocation : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentAddFavLocationBinding
    private lateinit var viewModel: FavLocationsViewModel
    private lateinit var adapter : FavLocationsListAdapter
    private val favLocationList = ArrayList<FavLocation>()
    private val addFavLocationViewModel = AddFavLocationViewModel.getInstance()
    private lateinit var placesAutoCompleteList: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dao = AppDatabase.getInstance(requireContext().applicationContext).favLocationsDao
        val repository = FavLocationsRepository(dao)
        val factory = FavLocationsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FavLocationsViewModel::class.java]
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_fav_location, container, false)
        binding.viewModel = viewModel
        binding.addFavLocationViewModel = addFavLocationViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.saveLocationBtn.setOnClickListener {
            val location = addFavLocationViewModel.location.value.toString()
            val title = binding.etLocationTitle.text.toString()
            viewModel.insert(FavLocation(0,title=title, location = location))
        }

        placesAutoCompleteList = binding.autoCompleteRv
        placesAutoCompleteList.layoutManager = LinearLayoutManager(context)
        placesAutoCompleteList.adapter = PlacesAutoCompleteListAdapter(listOf(), this, true)


        binding.etSelectedLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrBlank() || s.isEmpty()) binding.autoCompleteRv.visibility = View.GONE
            }
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.autoCompleteRv.visibility != View.VISIBLE) {
                    binding.autoCompleteRv.visibility = View.VISIBLE
                }
                if (binding.etSelectedLocation.isFocused && count>=2) {
                    getPlacesSuggestions(text.toString())
                    println(text)
                }
            }
        })

    }


    override fun onItemClick(position: Int, item: String) {
        println("$item is clicked!!")
        addFavLocationViewModel.location.value = item
        binding.autoCompleteRv.visibility = View.GONE
        findNavController().navigate(R.id.action_addFavLocation_to_chooseLocation)
    }

    override fun onSaveBtnClick(position: Int, item: String) {    }


    private fun getPlacesSuggestions(query: String) {
        var favLocationsList: List<String> = listOf()

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()


        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
//                .setLocationRestriction(bounds)
                .setCountries("IN")
                .setTypesFilter(listOf(PlaceTypes.ADDRESS))
                .setSessionToken(token)
                .setQuery(query)
                .build()

        val placesClient = Places.createClient(requireContext())
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                favLocationsList =  response.autocompletePredictions.map {
                    it.getPrimaryText(null).toString()
                }
                updateLocationSuggestionList(favLocationsList)
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("location", "Place not found: ${exception.statusCode}")
                }
            }
    }

    private fun updateLocationSuggestionList(newList: List<String>){
        (placesAutoCompleteList.adapter as? PlacesAutoCompleteListAdapter)?.updateData(newList)
    }

}