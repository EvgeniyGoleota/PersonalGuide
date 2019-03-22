package com.personalguide.restserveces

import android.content.Context
import com.google.android.gms.location.places.Place
import retrofit2.Callback

class PlaceApiManager(context: Context) : ApiManager(context) {

    private var iPlaces: IPlaces = retrofit.create(IPlaces::class.java)

    fun getGooglePlacesByLocationAndType(
        radius: Int,
        latitude: Double,
        longitude: Double,
        mapsKey: String,
        pageToken: String?,
        types: String,
        callback: Callback<PlaceResponse>) {
        if (pageToken == null) {
            val result = iPlaces.retrieve("json", "$latitude,$longitude", radius, mapsKey, types)
            result.enqueue(callback)
        } else {
            val nextPage = iPlaces.retrieve("json", pageToken, mapsKey)
            nextPage.enqueue(callback)
        }
    }
}