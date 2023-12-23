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
import com.kilomobi.washy.model.Feed

class FeedRepository : BaseRepository() {

    companion object {
        const val COLLECTION = "feeds"
        const val FEED_LIMIT = 20
    }

    private val db = FirebaseFirestore.getInstance()

    var feedList: MutableLiveData<ArrayList<Feed>> = MutableLiveData<ArrayList<Feed>>()

    fun retrieveMerchantFeed(merchantId: String) {
        val tmpListFeed: ArrayList<Feed> = ArrayList()

        db.collection(COLLECTION)
            .limit(FEED_LIMIT.toLong())
            .whereEqualTo("merchantId", merchantId)
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tmpListFeed.add(document.toObject(Feed::class.java))
                }
                feedList.value = tmpListFeed
                onDataReceived()
            }
            .addOnFailureListener {
                Log.d("bouhouhou", it.toString())
            }
    }
}