package com.example.vculp.shared.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vculp.R
import com.example.vculp.shared.data.models.MapData
import com.example.vculp.features.riderHome.ui.viewmodels.RiderViewModel
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import com.example.vculp.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MapFragment : Fragment() {

    private lateinit var riderViewModel: RiderViewModel
    private lateinit var apiKey: String
    private lateinit var mMap: GoogleMap
    lateinit var routeLine: Polyline
    private lateinit var userLocationViewModel: UserLocationViewModel
    private lateinit var previousMarker: Marker

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        googleMap.setMinZoomPreference(5f)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.style_json))
        clearMap()
        riderViewModel.coords.observe(viewLifecycleOwner){
            val coords = it
            if (coords != null) {
                drawRoute(coords)
            } else {
                Log.i("location_error", "mapCallback: can't draw maps with no coordinates!!")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        riderViewModel = RiderViewModel.getInstance()
        userLocationViewModel = UserLocationViewModel.getInstance()

        // to get the api key
        val applicationContext = requireContext().applicationContext
        val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName,PackageManager.GET_META_DATA)
        val value = ai.metaData.get("com.google.android.geo.API_KEY")
        apiKey = value.toString()


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        riderViewModel.dropLocation.observe(viewLifecycleOwner){
            riderViewModel.setCoords(requireContext())
        }

        userLocationViewModel.currentLocation.observe(viewLifecycleOwner){
            showCurrentLocation()
        }

    }

    private fun drawRoute(coords: Array<LatLng>){
        val originLocation = MarkerOptions().position(coords[0]).title("current location")
        val destinationLocation = MarkerOptions().position(coords[1]).title("drop location")
        clearMap()
        mMap.addMarker(originLocation)
        mMap.addMarker(destinationLocation)
        zoomMap(mMap, originLocation, destinationLocation)
        val urll = getDirectionURL(originLocation.position, destinationLocation.position, apiKey)
        getRoutePath(urll)
    }

    private fun clearMap(){
        Log.i("TAG", "clearMap: called")
        mMap.clear()
        if(this::routeLine.isInitialized) {
            routeLine.remove()
            Log.i("TAG", "clearMap: routeline removed")
        }
    }

    private fun zoomMap(map: GoogleMap,marker1: MarkerOptions, marker2: MarkerOptions){
        val builder = LatLngBounds.builder()
        builder.include(marker1.position)
        builder.include(marker2.position)
        val bounds = builder.build()
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,180))
    }

    private fun getDirectionURL(origin: LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    private fun getRoutePath(url:String){
        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            withContext(Dispatchers.Main){
                if(this@MapFragment::routeLine.isInitialized) {
                    routeLine.remove()
                    Log.i("TAG", "getRoutePath: routeline removed")
                }
            }
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                riderViewModel.setDuration(respObj.routes[0].legs[0].duration)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
                val lineoption = PolylineOptions()
                for (i in result.indices){
                    lineoption.addAll(result[i])
                    lineoption.width(14f)
                    val hsv = floatArrayOf(213.0f, 30.0f, 100.0f)
                    lineoption.color(Color.HSVToColor(hsv))
                    lineoption.geodesic(true)
                }
                withContext(Dispatchers.Main){
                    routeLine = mMap.addPolyline(lineoption)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun showCurrentLocation() {
        val lastKnownLocation: LatLng = LatLng(
            userLocationViewModel.currentLocation.value!!.latitude,
            userLocationViewModel.currentLocation.value!!.longitude
        )

        if(!this::mMap.isInitialized) return

        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    lastKnownLocation, Constants.MAP_ZOOM_LEVEL
                )
            )
        if (this::previousMarker.isInitialized) previousMarker.remove()


        // resizing the bitmapIcon
        val currentLocationMarkerIcon: Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.current_location),80,80,false)

        // showing information about that place.
        previousMarker = mMap.addMarker(MarkerOptions()
            .title("current location")
            .icon(BitmapDescriptorFactory.fromBitmap(currentLocationMarkerIcon))
            .position(lastKnownLocation)
            .snippet("current location"))!!

    }

}