package com.personalguide.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(private val position: LatLng,
                     private val title: String,
                     private val snippet: String) : ClusterItem {

    constructor(position: LatLng) : this(position, "", "")
    constructor(position: LatLng, title: String) : this(position, title, "")

    override fun getSnippet() = snippet

    override fun getTitle() = title

    override fun getPosition() = position
}