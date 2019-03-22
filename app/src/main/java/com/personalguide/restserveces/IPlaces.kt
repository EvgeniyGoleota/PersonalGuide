package com.personalguide.restserveces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IPlaces {

    @GET("maps/api/place/nearbysearch/{type}")
    fun retrieve(
        @Path("type") type: String, @Query("location") location: String,
        @Query("radius") radius: Int, @Query("key") key: String, @Query("types") types: String
    ): Call<PlaceResponse>

    @GET("maps/api/place/nearbysearch/{type}")
    fun retrieve(@Path("type") type: String, @Query("pagetoken") pageToken: String,
                          @Query("key") key: String): Call<PlaceResponse>
}