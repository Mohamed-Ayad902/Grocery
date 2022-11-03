package com.example.grocery.ui.account

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.grocery.databinding.FragmentLocationBinding
import com.example.grocery.models.User
import com.example.grocery.other.Constants
import com.example.grocery.other.Constants.ZOOM
import com.example.grocery.other.Resource
import com.example.grocery.other.showToast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "LocationFragment mohamed"

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationBinding
    private val userViewModel by activityViewModels<UserViewModel>()
    private var map: GoogleMap? = null
    private var locationLatLng = LatLng(25.0, 29.0) // just random default location
    private var locationPermissionGranted = false
    private var user: User? = null

    @Inject
    @Named(Constants.ENABLE_GPS_DIALOG)
    lateinit var enableGpsDialog: Dialog

    @Inject
    @Named(Constants.PERMISSION_DIALOG)
    lateinit var permissionDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            if (locationPermissionGranted && isGpsOpened()) {
                userViewModel.setUserLocation(locationLatLng)
                findNavController().popBackStack()
            }
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        userViewModel.user.collectLatest(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.w(TAG, "observe user details: loading")
                }
                is Resource.Success -> {
                    response.data?.let {
                        user = it
                        Log.d(
                            TAG,
                            "observe user details success: ${it.latitude}  --  ${it.longitude}"
                        )
                        locationLatLng = LatLng(it.latitude, it.longitude)
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, ZOOM))
                        map?.addMarker(MarkerOptions().position(locationLatLng))
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "observe user details: error -> ${response.message}")
                }
                is Resource.Idle -> {}
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        Log.d(TAG, "onMapReady: ")
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()
        // Get the current location of the device and set the position of the map.
        if (isGpsOpened() && user == null)
            getDeviceLocation()
        if (user != null) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        user!!.latitude,
                        user!!.longitude
                    ), ZOOM
                )
            )
            map.addMarker(MarkerOptions().position(LatLng(user!!.latitude, user!!.longitude)))
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQ_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQ_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    permissionDialog.show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        Log.d(TAG, "updateLocationUI: ")
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Log.d(TAG, "getDeviceLocation: ")
            if (locationPermissionGranted) {
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())

                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            Log.d(
                                TAG,
                                "getDeviceLocation: ${locationLatLng.latitude}   ${locationLatLng.longitude}"
                            )
                            locationLatLng = LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude
                            )
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation.latitude,
                                        lastKnownLocation.longitude
                                    ), ZOOM
                                )
                            )
                            map?.addMarker(
                                MarkerOptions()
                                    .position(
                                        LatLng(
                                            locationLatLng.latitude,
                                            locationLatLng.longitude
                                        )
                                    )
                            )

                            Log.d(
                                TAG,
                                "getDeviceLocation: ${locationLatLng.latitude}   ${locationLatLng.longitude}"
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(
                                    LatLng(
                                        26.0,
                                        32.0
                                    ), ZOOM
                                )
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            } else {
                Log.e(TAG, "getDeviceLocation: we don't have permission yet")
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        } catch (e: Exception) {
            Log.e(TAG, "getDeviceLocation: unExcepted exception", e)
        }
    }

    private fun isGpsOpened(): Boolean {
        val manager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGpsDialog.show()
            return false
        }
        showToast("gps opened")
        return true
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}