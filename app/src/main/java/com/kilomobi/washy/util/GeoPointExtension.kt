package com.kilomobi.washy.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint
import java.io.IOException
import java.util.*

fun GeoPoint.convertToAddress(context: Context) : String {
    val geoCoder = Geocoder(context, Locale.getDefault())

    try {
        val addresses: List<Address> = geoCoder.getFromLocation(
            this.latitude,
            this.longitude, 1)
        var add = ""
        if (addresses.isNotEmpty()) {
            add += addresses[0].getAddressLine(0).toString()
        }
        return add
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return this.toString()
}