package com.example.vculp.features.chooseLocation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.features.chooseLocation.ui.fragments.AutocompleteLocation


interface OnItemClickListener {
    fun onItemClick(position: Int, item: AutocompleteLocation )
    fun onSaveBtnClick(position: Int, item: AutocompleteLocation)
}


class LocationRecommendationsAdapter(private var locationsList: ArrayList<AutocompleteLocation>, private val listener: OnItemClickListener): RecyclerView.Adapter<LocationRecommendationsListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationRecommendationsListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.location_suggestions_list_item, parent, false)
        return LocationRecommendationsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    override fun onBindViewHolder(holder: LocationRecommendationsListViewHolder, position: Int) {
        holder.LocationAddress.text = locationsList[position].address
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, locationsList[position])
            onBindViewHolder(holder, position)
        }
        holder.SaveLocationBtn.setOnClickListener {
            listener.onSaveBtnClick(position, locationsList[position])
        }
    }


}

class LocationRecommendationsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val LocationAddress: TextView = itemView.findViewById(R.id.tvLocationAddressLocationSuggestionsItem)
    val SaveLocationBtn: ImageView = itemView.findViewById(R.id.saveLocationBtnLocationSuggestions)
}
