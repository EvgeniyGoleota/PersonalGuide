package com.personalguide.activities

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.personalguide.R
import com.personalguide.utils.*
import com.personalguide.viewmodels.ActivityVM

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var model: ActivityVM
    private var marker: Marker? = null
    private var placesUtils: PlacesUtils? = null
    private var filtersFAB: FloatingActionButton? = null
    private var filtersBox: CardView? = null
    private var mClusterManager: ClusterManager<MapClusterItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        initViewModel()
        initMap()
        initToolbar()
        initView()

        placesUtils = PlacesUtils(model, this, getPlaceType())
    }

    private fun getPlaceType(): HashMap<String, Array<String>> {
        val types = HashMap<String, Array<String>>()
        types[ACTIVITIES] = baseContext.resources.getStringArray(R.array.activities_places)
        types[VISIT] = baseContext.resources.getStringArray(R.array.visit_places)
        types[DINE] = baseContext.resources.getStringArray(R.array.dine_places)
        types[SHOP] = baseContext.resources.getStringArray(R.array.shop_places)
        return types
    }

    private fun initView() {
        filtersFAB = findViewById(R.id.filters_fab)
        filtersBox = findViewById(R.id.filters_box)

        setListeners()
    }

    private fun setListeners() {
        filtersFAB?.setOnClickListener {
            filtersBox?.let {
                if (filtersBox!!.visibility == View.GONE)
                    filtersBox!!.visibility = View.VISIBLE
                else filtersBox!!.visibility = View.GONE
            }
        }
    }

    private fun initToolbar() {

    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val fineLocationPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


            val listPermissionNeeded = mutableListOf<String>()

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) listPermissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)


            if (listPermissionNeeded.isNotEmpty()) {
                requestPermissions(listPermissionNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            } else {
                model.setPermissionState(true)
            }
        } else {
            model.setPermissionState(true)
        }
    }

    /**
     * A method initializes ViewModel and sets observers
     */
    private fun initViewModel() {
        model = ViewModelProviders.of(this).get(ActivityVM::class.java)
        initObservers()
    }

    private fun initObservers() {
        model.getPermissionState().observe(this, Observer { isPermissionGranted ->
            if (isPermissionGranted != null && isPermissionGranted) {
                getCurrentLocation()
            }
        })

        model.getCurrentLocation().observe(this, Observer { currentLocation ->
            currentLocation?.let {
                goToLocation(it)
                getPlaces()
            }
        })

        model.getPlacesList().observe(this, Observer { placeList ->
            placeList?.let {

            }
        })

        model.getListOfActivityPlaces().observe(this, Observer { placeList ->
            placeList?.let {
                for (place in it) {
                    mMap?.let { map -> MapUtils.addMarker(map, place.geometry.getLatLon(), null, place.name) }
                }
            }
        })

        model.getListOfVisitPlaces().observe(this, Observer { placeList ->
            placeList?.let {
                for (place in it) {
                    mMap?.let { map -> MapUtils.addMarker(map, place.geometry.getLatLon(), BitmapDescriptorFactory.HUE_GREEN, place.name) }
                }
            }
        })

        model.getListOfDinePlaces().observe(this, Observer { placeList ->
            placeList?.let {
                for (place in it) {
                    mMap?.let { map -> MapUtils.addMarker(map, place.geometry.getLatLon(), BitmapDescriptorFactory.HUE_BLUE, place.name) }
//                    mClusterManager?.addItem(MapClusterItem(place.geometry.getLatLon(), place.name))
                }
            }
        })

        model.getListOfShopPlaces().observe(this, Observer { placeList ->
            placeList?.let {
                for (place in it) {
                    mMap?.let { map -> MapUtils.addMarker(map, place.geometry.getLatLon(), BitmapDescriptorFactory.HUE_ORANGE, place.name) }
                }

            }
        })
    }

    private fun getPlaces() {
        placesUtils?.getNearbyPlaces(ACTIVITIES)
        placesUtils?.getNearbyPlaces(VISIT)
        placesUtils?.getNearbyPlaces(DINE)
        placesUtils?.getNearbyPlaces(SHOP)
    }

    /**
     * Moves the camera to the selected area, calculates the zoom and sets the marker to the selected location
     */
    private fun goToLocation(currentLocation: LatLng) {
        if (mMap == null) return

        val zoom = if (mMap!!.cameraPosition.zoom > MAP_CURRENT_LOCATION_ZOOM) {
            mMap!!.cameraPosition.zoom
        } else {
            MAP_CURRENT_LOCATION_ZOOM.toFloat()
        }

        if (marker == null) {
            marker = mMap!!.addMarker(MarkerOptions().position(currentLocation))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoom))
        } else {
            if (marker!!.isInfoWindowShown) marker!!.hideInfoWindow()
            marker!!.position = currentLocation
            marker!!.showInfoWindow()
        }
    }

    /**
     * This method retrieves device last know location
     */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
//                val londonLatLng = LatLng(51.505703, -0.110987)
//                val londonLatLng = LatLng(48.855089, 2.357835)
                val londonLatLng = LatLng(40.762503, -73.975399)
                model.setCurrentLocation(londonLatLng)
//                model.setCurrentLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED)
                if (grantResults.isNotEmpty()) {
                    for ((i, value) in permissions.withIndex())
                        perms.put(permissions[i], grantResults[i])

                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        model.setPermissionState(true)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            if (shouldShowRequestPermissionRationale(Manifest.permission_group.LOCATION)) {
//                                showDialogOK("Some permissions required for this application",
//                                    DialogInterface.OnClickListener { dialog, which ->
//                                        when (which) {
//                                            DialogInterface.BUTTON_POSITIVE ->
//                                                checkAndRequestPermissions()
//                                            DialogInterface.BUTTON_NEGATIVE ->
//                                                dialog?.dismiss()
//                                        }
//                                    })
                            } else {
                                Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpCluster(mMap!!)
    }

    private fun setUpCluster(mMap: GoogleMap) {
        mClusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(mClusterManager)
    }
}
