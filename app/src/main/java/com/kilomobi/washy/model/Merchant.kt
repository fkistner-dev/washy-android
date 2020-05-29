package com.kilomobi.washy.model

import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class Merchant(
    var name: String,
    var presentation: String? = null,
    var position: GeoPoint? = null,
    var priceStart: Double,
    var avgRating: Float,
    var numRating: Int,
    var services: String?,
    var adminId: String
//    @ColumnInfo(name = SERVICE_CERAMIC)
//    var serviceCeramic: Boolean = false,
//    @ColumnInfo(name = SERVICE_ECO_FRIENDLY)
//    var serviceEcoFriendly: Boolean = false,
//    @ColumnInfo(name = SERVICE_TRANSPORT)
//    var serviceTransport: Boolean = false,
//    @ColumnInfo(name = SERVICE_HEADLIGHT)
//    var serviceHeadlight: Boolean = false,
//    @ColumnInfo(name = SERVICE_BIKE)
//    var serviceBike: Boolean = false,
//    @ColumnInfo(name = SERVICE_ELECTRIC_PLUG)
//    var serviceElectricPlug: Boolean = false,
//    @ColumnInfo(name = SERVICE_COFFEE)
//    var serviceCoffee: Boolean = false,
//    @ColumnInfo(name = SERVICE_WIFI)
//    var serviceWifi: Boolean = false
) : RecyclerItem(), AdapterClick {
    constructor() : this("", "", GeoPoint(0.0,0.0), 0.0, 0.0f, 0, "", "0")
    companion object {
        const val SERVICES_DELIMITER = ","
    }
    // wifi,coffee,headlight
    // , is the delimiter

}