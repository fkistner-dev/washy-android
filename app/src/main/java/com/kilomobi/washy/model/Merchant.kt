package com.kilomobi.washy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class Merchant(
    @Exclude var reference: Any? = null,
    var name: String = "",
    var description: String? = "",
    var geohash: String? = "",
    var position: GeoPoint? = GeoPoint(0.0,0.0),
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
) : RecyclerItem(), AdapterClick, Parcelable {
    companion object CREATOR : Parcelable.ClassLoaderCreator<Merchant> {
        override fun createFromParcel(parcel: Parcel): Merchant {
            return Merchant(parcel)
        }
        override fun createFromParcel(source: Parcel?, loader: ClassLoader?): Merchant {
            return Merchant()
        }

        override fun newArray(size: Int): Array<Merchant?> {
            return arrayOfNulls(size)
        }

    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeDouble(position!!.latitude)
        parcel.writeDouble(position!!.longitude)
    }

    override fun describeContents(): Int {
        return 0
    }
}