package com.personalguide.restserveces

import com.personalguide.restserveces.models.Place
import java.util.ArrayList

class PlaceResponse(private var next_page_token: String,
                    private var results: ArrayList<Place>) {


    fun getNext_page_token(): String? {
        return next_page_token
    }

    fun setNext_page_token(next_page_token: String) {
        this.next_page_token = next_page_token
    }

    fun getResults() = results
}