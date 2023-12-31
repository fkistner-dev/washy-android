/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.model.Merchant
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.getAtLocation
import kotlin.random.Random

class MerchantListRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "merchants"
        const val RADIUS_IN_METER = 20000.0 // 20 km
    }

    private val db = FirebaseFirestore.getInstance()
    var merchantList: MutableLiveData<ArrayList<Merchant>> = MutableLiveData()

    fun retrieveMerchantList() {
        val tmpListMerchant: ArrayList<Merchant> = ArrayList()

        db.collection(COLLECTION)
            .whereEqualTo("active", true)
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val merchant = document.toObject(Merchant::class.java)
                    merchant.reference = document.id
                    merchant.avgRating = Random.nextInt(3,5).toFloat()
                    tmpListMerchant.add(merchant)
                }
                merchantList.value = tmpListMerchant
                onDataReceived()
            }
    }

    fun retrieveMerchant(latitude: Double, longitude: Double) {
        val tmpListMerchant: ArrayList<Merchant> = ArrayList()
        val geoFirestore = GeoFirestore(db.collection(COLLECTION))

        geoFirestore.getAtLocation(GeoPoint(latitude, longitude), RADIUS_IN_METER) { docs, ex ->
            if (ex == null) {
                if (docs != null) {
                    for (document in docs) {
                        val merchant = document.toObject(Merchant::class.java)
                        merchant?.let {
                            merchant.reference = document.id
                            tmpListMerchant.add(merchant)
                        }
                    }
                    merchantList.value = tmpListMerchant
                    onDataReceived()
                }
            } else {
                Log.e(MerchantListRepository::class.java.canonicalName, "onError: ", ex)
                return@getAtLocation
            }
        }
    }
}