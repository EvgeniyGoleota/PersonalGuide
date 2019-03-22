package com.personalguide.restserveces.models

import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

class Place(
    var id: String,
    var icon: String,
    var name: String,
    var place_id: String,
    var reference: String,
    var vicinity: String,
    var types: ArrayList<String>,
    var photos: ArrayList<PlacesPhoto>,
    var geometry: Geometry,
    var latitude: Double,
    var longitude: Double,
    var photoReference: String,
    var formatted_phone_number: String,
    var imageUrl: String,
    var banner: Boolean
) {

    inner class Geometry(var location: Location) {
        fun getLatLon() = LatLng(location.lat, location.lng)
    }

    inner class Location(var lat: Double,
                         var lng: Double)
}