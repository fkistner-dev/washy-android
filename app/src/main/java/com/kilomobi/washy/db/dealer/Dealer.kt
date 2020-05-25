package com.kilomobi.washy.db.dealer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

@Entity(tableName = Dealer.TABLE_NAME)
data class Dealer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UID)
    val uid: Long = 0,
    @ColumnInfo(name = ID)
    override val id: String? = "0",
    @ColumnInfo(name = NAME)
    var name: String,
    @ColumnInfo(name = PRESENTATION)
    var presentation: String? = null,
    @ColumnInfo(name = ADDRESS)
    var address: String? = null,
    @ColumnInfo(name = LATITUDE)
    var latitude: Float,
    @ColumnInfo(name = LONGITUDE)
    var longitude: Float,
    @ColumnInfo(name = PRICE_RANGE)
    var priceStart: Double,
    @ColumnInfo(name = RATING)
    var rating: Float,
    @ColumnInfo(name = SERVICES)
    var services: String?
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
) : RecyclerItem, AdapterClick {
    companion object {
        const val TABLE_NAME = "dealer"
        const val UID = "uid"
        const val ID = "id"
        const val NAME = "name"
        const val PRESENTATION = "presentation"
        const val ADDRESS = "address"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val PRICE_RANGE = "price_range"
        const val RATING = "rating"
//        const val SERVICE_CERAMIC = "service_ceramic"
//        const val SERVICE_ECO_FRIENDLY = "service_eco_friendly"
//        const val SERVICE_TRANSPORT = "service_transportation"
//        const val SERVICE_HEADLIGHT = "service_headlight"
//        const val SERVICE_BIKE = "service_bike"
//        const val SERVICE_ELECTRIC_PLUG = "service_electric_plug"
//        const val SERVICE_COFFEE = "service_coffee"
//        const val SERVICE_WIFI = "service_wifi"
        const val SERVICES = "services"
        const val SERVICES_DELIMITER = ","
    }
    // wifi,coffee,headlight
    // , is the delimiter

}