package com.kilomobi.washy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

data class Merchant(
    var reference: Any? = null,
    var name: String = "",
    var description: String? = null,
    var position: GeoPoint? = null,
    var priceStart: Double = 0.0,
    var phone: String = "",
    var avgRating: Float = 0f,
    var numRating: Int = 0,
    var services: ArrayList<String> = ArrayList(),
    var siren: String = "",
    var workAtCustomer: Boolean = false,
    var isActive: Boolean = false,
    var adminId: String = "",
    var favoriteId: ArrayList<String> = ArrayList()
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