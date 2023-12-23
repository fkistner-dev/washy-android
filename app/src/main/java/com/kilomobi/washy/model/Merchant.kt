/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem
import java.io.Serializable

data class Merchant(
    @Exclude var reference: String? = null,
    var name: String = "",
    var description: String? = "",
    var geohash: String? = "",
    @Transient var position: GeoPoint? = GeoPoint(0.0,0.0), // GeoPoint not serializable when starting Intent
    var fullAddress: String? = "",
    var website: String? = "",
    var phone: String? = "",
    var imgUrl: String? = "",
    var avgRating: Float = 0f,
    var numRating: Int = 0,
    var services: ArrayList<String> = ArrayList(),
    var siren: String? = "",
    var workAtCustomer: Boolean = false,
    var active: Boolean = false,
    var verified: Boolean = false,
    var imported: Boolean = false,
    var adminId: String? = "",
    var week: Map<String, String>? = mapOf()
) : RecyclerItem(), AdapterClick, Serializable