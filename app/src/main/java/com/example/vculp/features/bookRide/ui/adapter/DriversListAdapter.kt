package com.example.vculp.features.bookRide.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vculp.R
import com.example.vculp.shared.data.models.DriverApiData
import com.example.vculp.shared.data.models.DriverApiDataItem

interface OnItemClickListener {
    fun onItemClick(position: Int, item: DriverApiDataItem)
}
class DriversListAdapter(private var driversData: DriverApiData, private val negotiateBtnClickListener: (position:Int,item:DriverApiDataItem)->Unit,private val acceptBtnClickListener: (position:Int,item:DriverApiDataItem)->Unit): RecyclerView.Adapter<DriversListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.drivers_list_item, parent, false)
        return DriversListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return driversData.size
    }

    override fun onBindViewHolder(holder: DriversListViewHolder, position: Int) {
        Log.i("drivers_list", "onBindViewHolder: $driversData")
        val imageUri  = driversData[position].imageUri
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .into(holder.image)
        holder.vehicleType.text = driversData[position].vehicleType
        holder.vehicleNumber.text = driversData[position].vehicleNo
        holder.ridePrice.text = driversData[position].ridePrice
        holder.negotiateBtn.setOnClickListener {
            negotiateBtnClickListener(position,driversData[position])
        }
        holder.acceptBtn.setOnClickListener {
            acceptBtnClickListener(position,driversData[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(driversDataArray: DriverApiData) {
        driversData = driversDataArray
        notifyDataSetChanged()
    }

}

class DriversListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val vehicleType: TextView = itemView.findViewById(R.id.driverEta)
    val vehicleNumber: TextView = itemView.findViewById(R.id.driverName)
    val ridePrice: TextView = itemView.findViewById(R.id.tvRidePrice)
    val image: ImageView = itemView.findViewById(R.id.driverProfileImage)
    val negotiateBtn: TextView = itemView.findViewById(R.id.tvNegotiateBtn)
    val acceptBtn: TextView = itemView.findViewById(R.id.tvAcceptBtn)
}