package com.personalguide.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.personalguide.viewmodels.ActivityVM

class PlacesUtil(private val viewModel: ActivityVM, private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getNearbyPlaces() {
        val placeClient: PlaceDetectionClient = Places.getPlaceDetectionClient(context)
        val placeResult = placeClient.getCurrentPlace(null)
        placeResult.addOnCompleteListener {
            it.result?.let { result ->
                viewModel.setPlaceResponse(result)
            }
        }
    }

}