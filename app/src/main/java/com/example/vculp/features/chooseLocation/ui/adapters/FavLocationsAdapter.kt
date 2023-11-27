package com.example.vculp.features.chooseLocation.ui.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FavLocationsListItemBinding
import com.example.vculp.shared.data.models.FavRegionDataItem

class FavLocationsListAdapter( private val onClickListener: (FavRegionDataItem)->Unit, private val onLongPressListener: (FavRegionDataItem)->Unit): RecyclerView.Adapter<FavLocationsListViewHolder>() {

    private val locationsList= ArrayList<FavRegionDataItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavLocationsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FavLocationsListItemBinding>(inflater,R.layout.fav_locations_list_item, parent, false)
        return FavLocationsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavLocationsListViewHolder, position: Int) {
        holder.bind(locationsList[position],onClickListener, onLongPressListener)
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListItems(newListItems : List<FavRegionDataItem>){
        locationsList.clear()
        locationsList.addAll(newListItems)
    }
}

class FavLocationsListViewHolder(val binding: FavLocationsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: FavRegionDataItem, clickListener: (FavRegionDataItem)->Unit, longPressListener: (FavRegionDataItem)-> Unit){
        binding.tvFavLocationAddress.text = location.name
        binding.tvFavLocationTitle.text = location.areaName
        binding.favLocationListItem.setOnClickListener {
            clickListener(location)
        }
        binding.favLocationListItem.setOnLongClickListener {
            longPressListener(location)
            return@setOnLongClickListener false
        }
    }
}
