package com.kilomobi.washy.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kilomobi.washy.model.Feed

class FeedRepository {

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
            }
            .addOnFailureListener {
                Log.d("bouhouhou", it.toString())
            }
    }
}