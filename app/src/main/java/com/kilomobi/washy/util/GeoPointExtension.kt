/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

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
        val addresses: MutableList<Address>? = geoCoder.getFromLocation(
            this.latitude,
            this.longitude, 1)
        var add = ""
        if (addresses?.isNotEmpty() == true) {
            add += addresses[0].getAddressLine(0).toString()
        }
        return add
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return this.toString()
}