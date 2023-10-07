package com.example.vculp


import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.navigation.fragment.NavHostFragment
import com.example.vculp.databinding.ActivityMainBinding
import com.example.vculp.utils.CustomLocationManager
import com.example.vculp.shared.ui.viewmodels.UserLocationViewModel
import com.example.vculp.utils.Constants
import com.example.vculp.utils.PermissionsHandler


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val locationPermissionCode = Constants.LOCATION_PERMISSION_CODE
    private lateinit var locationManager: CustomLocationManager
    private lateinit var userLocationViewModel: UserLocationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val permissionsHandler = PermissionsHandler()
        userLocationViewModel = UserLocationViewModel.getInstance()
        locationManager = CustomLocationManager(this)
        setContentView(binding.root)
    }

    private fun navigateToLoginScreen() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView4) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_riderFragment_to_loginHome)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, perform your location-related tasks
                // Implement your location-related functionality here
            } else {
                // Location permissions denied, handle the scenario where the user denied permissions
                Toast.makeText(this, "Permissions are required!!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
