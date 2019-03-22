package com.personalguide.restserveces

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiManager(context: Context) {
    protected lateinit var retrofit: Retrofit
    private val serverAddress: String = "https://maps.googleapis.com/"

    init {
        initRetrofit(serverAddress)
    }

    @Synchronized fun initRetrofit(serverAddress:  String) {
        retrofit = Retrofit.Builder()
            .baseUrl(serverAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}