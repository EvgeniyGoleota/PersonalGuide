package com.personalguide.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapUtils {

    companion object {
        fun addMarkerWithNumber(
            map: GoogleMap,
            text: String,
            position: LatLng,
            markerColor: Float? = null,
            isDraggable: Boolean = false
        ): Marker {
            val conf = Bitmap.Config.ARGB_8888
            val bmp = Bitmap.createBitmap(30, 150, conf)
            val canvas = Canvas(bmp)
            val paint = Paint()
            paint.color = Color.WHITE
            paint.textSize = 25F
            paint.style = Paint.Style.FILL
            canvas.drawText(text, 6F, 72F, paint)
            val marker = map.addMarker(MarkerOptions().apply {
                position(position)
                draggable(isDraggable)
                if (markerColor != null) {
                    icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                }
            })
            map.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(bmp)))
            return marker
        }

        fun addMarker(
            map: GoogleMap,
            position: LatLng,
            markerColor: Float? = null,
            title: String? = null,
            isDraggable: Boolean = false
        ): Marker {
            return map.addMarker(MarkerOptions().apply {
                position(position)
                draggable(isDraggable)
                snippet(title)
                if (markerColor != null) {
                    icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                }
            })
        }
    }
}