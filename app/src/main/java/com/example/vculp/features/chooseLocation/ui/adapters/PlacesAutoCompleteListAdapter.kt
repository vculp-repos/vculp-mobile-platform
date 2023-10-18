package com.example.vculp.features.chooseLocation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R

class PlacesAutoCompleteListAdapter(
    private var suggestionsList: List<String>,
    private val listener: OnItemClickListener,
    private val hideSaveBtn: Boolean = false
): RecyclerView.Adapter<PlacesAutoCompleteListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesAutoCompleteListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.location_suggestions_list_item,parent,false)
        return PlacesAutoCompleteListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return suggestionsList.size
    }

    override fun onBindViewHolder(holder: PlacesAutoCompleteListViewHolder, position: Int) {
        holder.LocationAddress.text = suggestionsList[position]
        hideSaveBtn.apply {
            holder.itemView.setOnClickListener {
                listener.onItemClick(position, suggestionsList[position])
                onBindViewHolder(holder, position)
            }
            if(this) {
                holder.SaveLocationBtn.visibility = View.GONE
            }else {
                holder.SaveLocationBtn.setOnClickListener {
                    listener.onSaveBtnClick(position, suggestionsList[position])
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<String>) {
        suggestionsList = newList
        notifyDataSetChanged()
    }
}

class PlacesAutoCompleteListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val LocationAddress: TextView = itemView.findViewById(R.id.tvLocationAddressLocationSuggestionsItem)
    val SaveLocationBtn: ImageView = itemView.findViewById(R.id.saveLocationBtnLocationSuggestions)
}