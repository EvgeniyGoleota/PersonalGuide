package com.personalguide.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.maps.model.LatLng

class ActivityVM : ViewModel() {
    private val mPermissionState = MutableLiveData<Boolean>()
    private val currentLocation = MutableLiveData<LatLng>()
    private val placeResponse = MutableLiveData<PlaceLikelihoodBufferResponse>()
    private val placesList = MutableLiveData<List<Place>>()


    fun getPermissionState() = mPermissionState

    fun setPermissionState(state: Boolean) {
        mPermissionState.value = state
    }

    fun getCurrentLocation() = currentLocation

    fun setCurrentLocation(currentLocation: LatLng) {
        this.currentLocation.value = currentLocation
    }

    fun setPlaceResponse(placeResponse: PlaceLikelihoodBufferResponse) {
        this.placeResponse.value = placeResponse
        retrievePlaces()
    }

    private fun retrievePlaces() {
        placeResponse.value?.let {
            val list = mutableListOf<Place>()
            for (placeLikelihood in placeResponse.value!!) {
                list.add(placeLikelihood.place.freeze())
            }
            placesList.value = list
        }
    }

    fun getPlacesList() = placesList
}