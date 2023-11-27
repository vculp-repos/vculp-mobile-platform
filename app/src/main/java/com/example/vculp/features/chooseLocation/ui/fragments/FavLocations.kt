package com.example.vculp.features.chooseLocation.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vculp.R
import com.example.vculp.databinding.FragmentFavLocationsBinding
import com.example.vculp.features.chooseLocation.data.FavLocationsRepository
import com.example.vculp.features.chooseLocation.data.models.FavLocation
import com.example.vculp.features.chooseLocation.ui.adapters.FavLocationsListAdapter
import com.example.vculp.features.chooseLocation.ui.viewmodels.AddFavLocationViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModel
import com.example.vculp.shared.ui.viewmodels.FavLocationsViewModelFactory
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.data.AppDatabase
import com.example.vculp.shared.data.models.FavRegionDataItem
import com.google.android.material.snackbar.Snackbar

class FavLocations : Fragment() {

    companion object {
        fun newInstance() = FavLocations()
    }

    private lateinit var viewModel: FavLocationsViewModel
    private lateinit var riderViewModel: RiderViewModel
    private lateinit var binding: FragmentFavLocationsBinding
    private lateinit var adapter: FavLocationsListAdapter
    private val favLocationList = ArrayList<FavRegionDataItem>()
    private lateinit var addFavLocationViewModel: AddFavLocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        riderViewModel = RiderViewModel.getInstance()
        addFavLocationViewModel = AddFavLocationViewModel.getInstance()
        val dao = AppDatabase.getInstance(requireContext().applicationContext).favLocationsDao
        val repository = FavLocationsRepository(dao)
        val favLocationsViewModel: FavLocationsViewModel by activityViewModels {
            FavLocationsViewModelFactory(repository)
        }
        viewModel = favLocationsViewModel
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_fav_locations, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavLocationsListAdapter({ selectedItem: FavRegionDataItem ->
            onItemClick(selectedItem)
        },
            { selectedItem: FavRegionDataItem ->
                onItemLongPressListener(selectedItem)
            }
        )
        binding.recyclerView.adapter = adapter

        viewModel.getAllLocations()
        displayFavLocationList()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val bg: Drawable? = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.list_item_background_delete
                )
                bg?.setBounds(
                    0, viewHolder.itemView.top,
                    (viewHolder.itemView.left + dX).toInt(), viewHolder.itemView.bottom
                );
                bg?.draw(c)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    val deletedLocation: FavRegionDataItem =
                        favLocationList.get(viewHolder.adapterPosition)
                    val position = viewHolder.adapterPosition
                    viewModel.delete(deletedLocation)

                    println("deleting ${deletedLocation.name} ${deletedLocation.areaName}")
                    // below line is to display our snackbar with action.
                    Snackbar.make(
                        binding.recyclerView,
                        "Deleted " + deletedLocation.name,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(
                            "Undo",
                            View.OnClickListener {
                                viewModel.insert(
                                    FavLocation(
                                        title = deletedLocation.name,
                                        address = deletedLocation.areaName,
                                        longitude = deletedLocation.longitude,
                                        latitude = deletedLocation.latitude
                                    )
                                )
                                adapter.notifyItemInserted(position)
                            }).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "failed to delete location", Toast.LENGTH_LONG).show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayFavLocationList() {
        viewModel.favLocations.observe(viewLifecycleOwner) {
            favLocationList.clear()
            favLocationList.addAll(it)
            adapter.updateListItems(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onItemClick(item: FavRegionDataItem) {
        riderViewModel.setDropLocation(item.areaName)
        findNavController().navigate(R.id.action_favLocations_to_chooseLocation)
    }

    private fun onItemLongPressListener(item: FavRegionDataItem) {
        addFavLocationViewModel.toUpdate.value = true
        viewModel.setSelectedLocation(item)
        addFavLocationViewModel.location.value = item.areaName

        findNavController().navigate(R.id.action_favLocations_to_addFavLocation)
    }


}