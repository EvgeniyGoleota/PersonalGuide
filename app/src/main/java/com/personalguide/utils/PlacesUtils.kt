package com.personalguide.utils

import android.annotation.SuppressLint
import android.content.Context
import com.personalguide.restserveces.PlaceApiManager
import com.personalguide.restserveces.PlaceResponse
import com.personalguide.viewmodels.ActivityVM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesUtils(
    private val viewModel: ActivityVM,
    private val context: Context,
    private val placesTypeMap: HashMap<String, Array<String>>
) {

    @SuppressLint("MissingPermission")
    fun getNearbyPlaces(groupOfType: String, nextPageToken: String? = null) {
        val placeManager = PlaceApiManager(context)
        viewModel.getCurrentLocation().value?.let { location ->
            placeManager.getGooglePlacesByLocationAndType(5000,
                location.latitude,
                location.longitude,
                GOOGLE_BROWSER_API_KEY,
                nextPageToken,
                getTypesByTag(groupOfType),
                object : Callback<PlaceResponse> {
                    override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                        if (response.isSuccessful) {
                            val result = response.body()?.getResults()
                            result?.let {
                                if (result.size != 0)
                                    when (groupOfType) {
                                        ACTIVITIES -> viewModel.addListOfActivityPlaces(it)
                                        VISIT -> viewModel.addListOfVisitPlaces(it)
                                        DINE -> viewModel.addListOfDinePlaces(it)
                                        SHOP -> viewModel.addListOfShopPlaces(it)
                                    }

                                response.body()?.let { response ->
                                    if (response.getNext_page_token() != null) {
                                        android.os.Handler().postDelayed({
                                            getNearbyPlaces(groupOfType, response.getNext_page_token())
                                        }, 2000)
                                    }
                                }
                            }

                            viewModel.addPlacesList(response.body()?.getResults()!!)
                        }
                    }

                    override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }

    private fun getTypesByTag(groupOfType: String): String {
        val typesArray = placesTypeMap[groupOfType]

        typesArray?.let { array ->
            val sb = StringBuilder()

            val lastPosition = array.size - 1

            for ((count, type) in array.withIndex()) {
                if (count < lastPosition) {
                    sb.append(type)
                    sb.append("||")
                } else {
                    sb.append(type)
                }

            }
            return sb.toString()
        }
        return ""
    }

}