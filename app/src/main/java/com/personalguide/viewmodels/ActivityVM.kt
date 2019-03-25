package com.personalguide.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.personalguide.restserveces.models.Place

class ActivityVM : ViewModel() {
    private val mPermissionState = MutableLiveData<Boolean>()
    private val currentLocation = MutableLiveData<LatLng>()
    private val placesList = MutableLiveData<MutableList<Place>>()
    private val placesListActivities = MutableLiveData<MutableList<Place>>()
    private val placesListVisit = MutableLiveData<MutableList<Place>>()
    private val placesListDine = MutableLiveData<MutableList<Place>>()
    private val placesListShop = MutableLiveData<MutableList<Place>>()


    fun getPermissionState() = mPermissionState

    fun setPermissionState(state: Boolean) {
        mPermissionState.value = state
    }

    fun getCurrentLocation() = currentLocation

    fun setCurrentLocation(currentLocation: LatLng) {
        this.currentLocation.value = currentLocation
    }

    fun addPlacesList(placeResponse: List<Place>) {
        if (this.placesList.value == null) this.placesList.value = mutableListOf()
        this.placesList.value!!.addAll(placeResponse)
    }

    fun getPlacesList() = placesList

    fun addListOfActivityPlaces(placeResponse: List<Place>) {
        if (this.placesListActivities.value == null)
            this.placesListActivities.value = placeResponse as MutableList<Place>
        else
            this.placesListActivities.value!!.addAll(placeResponse)

    }

    fun getListOfActivityPlaces() = placesListActivities

    fun addListOfVisitPlaces(placeResponse: List<Place>) {
        if (this.placesListVisit.value == null)
            this.placesListVisit.value = placeResponse as MutableList
        else
            this.placesListVisit.value!!.addAll(placeResponse)
    }

    fun getListOfVisitPlaces() = placesListVisit

    fun addListOfDinePlaces(placeResponse: List<Place>) {
        if (this.placesListDine.value == null)
            this.placesListDine.value = placeResponse as MutableList
        else
            this.placesListDine.value!!.addAll(placeResponse)
    }

    fun getListOfDinePlaces() = placesListDine

    fun addListOfShopPlaces(placeResponse: List<Place>) {
        if (this.placesListShop.value == null)
            this.placesListShop.value = placeResponse as MutableList
        else
            this.placesListShop.value!!.addAll(placeResponse)
    }

    fun getListOfShopPlaces() = placesListShop
}