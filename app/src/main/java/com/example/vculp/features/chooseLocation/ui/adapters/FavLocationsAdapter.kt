package com.example.vculp.features.chooseLocation.ui.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FavLocationsListItemBinding
import com.example.vculp.features.chooseLocation.data.models.FavLocation

class FavLocationsListAdapter( private val listener: (FavLocation)->Unit): RecyclerView.Adapter<FavLocationsListViewHolder>() {

    private val locationsList= ArrayList<FavLocation>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavLocationsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FavLocationsListItemBinding>(inflater,R.layout.fav_locations_list_item, parent, false)
        return FavLocationsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavLocationsListViewHolder, position: Int) {
        holder.bind(locationsList[position],listener)
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    fun removeListItem(newListItems: List<FavLocation>){
        locationsList.clear()
        locationsList.addAll(newListItems)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListItems(newListItems : List<FavLocation>){
        locationsList.clear()
        locationsList.addAll(newListItems)
    }
}

class FavLocationsListViewHolder(val binding: FavLocationsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: FavLocation, clickListener: (FavLocation)->Unit){
        binding.tvFavLocationAddress.text = location.location
        binding.tvFavLocationTitle.text = location.title
        binding.favLocationListItem.setOnClickListener {
            clickListener(location)
        }
    }
}
